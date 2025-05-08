package com.financetracker.model;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets; // 明确指定字符编码
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DeepSeekClient {
    private static final String API_KEY = "sk-da2acc7c58c3459996121ce62df14a19"; // 示例密钥，请替换为您自己的
    private static final String API_URL = "https://api.deepseek.com/v1/chat/completions";
    private final HttpClient httpClient; // HttpClient 实例，建议复用

    // 定义用户数据的基础目录
    private static final String USER_DATA_BASE_PATH = "/Users/gerry/Desktop/软件工程/FinanceTracker/data/";

    /**
     * 构造函数，初始化 HttpClient
     */
    public DeepSeekClient() {
        this.httpClient = HttpClient.newHttpClient();
    }

    /**
     * 读取指定路径文件的内容。
     *
     * @param filePath 文件路径字符串。
     * @return 文件内容字符串。
     * @throws IOException 如果读取文件时发生 I/O 错误。
     */
    private String readFileContent(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        // 使用 UTF-8 编码读取，确保中文等字符正确处理
        return Files.readString(path, StandardCharsets.UTF_8);
    }

    /**
     * 根据用户ID获取其交易数据的财务分析。
     * 它会读取该用户的 categories.csv 和 transactions.csv 文件，
     * 将数据发送给 DeepSeek API，并返回分析结果。
     *
     * @param userId 用户的唯一标识符。
     * @return DeepSeek API 返回的分析结果字符串 (通常是 JSON 格式)。
     * @throws IOException 如果读取用户的 CSV 文件时出错 (例如文件不存在或无权限)。
     * @throws InterruptedException 如果 HTTP 请求被中断。
     * @throws RuntimeException 如果 API 调用失败或返回错误状态码。
     */
    public String getFinancialAnalysis(String userId)
            throws IOException, InterruptedException {

        // 1. 构建用户数据文件的完整路径
        Path userDir = Paths.get(USER_DATA_BASE_PATH, userId);
        Path categoriesCsvPath = userDir.resolve("categories.csv");
        Path transactionsCsvPath = userDir.resolve("transactions.csv");

        // 2. 读取 CSV 文件内容
        String categoriesData;
        String transactionsData;
        try {
            categoriesData = readFileContent(categoriesCsvPath.toString());
            transactionsData = readFileContent(transactionsCsvPath.toString());
        } catch (IOException e) {
            // 如果文件读取失败（比如文件不存在），抛出更明确的异常信息
            throw new IOException("无法读取用户 " + userId + " 的数据文件 (" + categoriesCsvPath + " 或 " + transactionsCsvPath + "): " + e.getMessage(), e);
        }

        // 3. 构建发送给 API 的输入文本 (Prompt)
        // 添加了更清晰的分隔符和上下文提示，帮助 AI 理解数据结构
        String inputText = String.format("""
                请根据以下提供的特定用户的交易记录和类别信息，对该用户的消费进行洞察、预测和建议。

                --- 用户类别数据 (categories.csv) ---
                %s
                --- 用户交易数据 (transactions.csv) ---
                %s
                --- 数据结束 ---

                请基于以上数据进行分析。""", categoriesData, transactionsData);

        // 4. 准备 API 请求的 JSON 载荷

        String jsonInput = String.format("""
            {
                "model": "deepseek-chat",
                "messages": [
                    {"role": "user", "content": "%s"}
                ],
                "temperature": 0.7
            }
            """, escapeJsonString(inputText)); // 对输入文本进行转义

        // 5. 构建 HTTP 请求
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(jsonInput))
                .build();

        // 6. 发送请求并获取响应
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // 7. 检查响应状态码
        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            // 成功，返回响应体（通常是包含分析结果的 JSON 字符串）

            return response.body();
        } else {
            // API 调用失败，抛出运行时异常，携带状态码和错误信息
            throw new RuntimeException("DeepSeek API 调用失败，状态码: " + response.statusCode() + ", 响应体: " + response.body());
        }
    }

    /**
     * 对字符串进行转义，使其能安全地嵌入到 JSON 字符串值中。
     * 处理常见的特殊字符，如引号、反斜杠和控制字符。
     *
     * @param input 原始字符串。
     * @return JSON 转义后的字符串。
     */
    private String escapeJsonString(String input) {
        if (input == null) {
            return null;
        }
        StringBuilder escaped = new StringBuilder();
        for (char c : input.toCharArray()) {
            switch (c) {
                case '"': escaped.append("\\\""); break;
                case '\\': escaped.append("\\\\"); break;
                case '\b': escaped.append("\\b"); break;
                case '\f': escaped.append("\\f"); break;
                case '\n': escaped.append("\\n"); break; // 对换行符进行转义
                case '\r': escaped.append("\\r"); break; // 对回车符进行转义
                case '\t': escaped.append("\\t"); break; // 对制表符进行转义
                // 可能需要转义其他控制字符 (U+0000 到 U+001F)
                default:
                    if (c < ' ') {
                        // 使用 Unicode 表示法转义控制字符
                        String hex = Integer.toHexString(c);
                        escaped.append("\\u");
                        for (int k = 0; k < 4 - hex.length(); k++) {
                            escaped.append('0');
                        }
                        escaped.append(hex.toUpperCase());
                    } else {
                        escaped.append(c);
                    }
            }
        }
        return escaped.toString();
    }


    // --- main 方法：用于演示如何调用 ---
    public static void main(String[] args) {
        DeepSeekClient client = new DeepSeekClient();
        // 示例用户 ID，请替换为实际存在的用户 ID
        String testUserId = "170db30a-45d3-4403-a2e1-2987a54afaa1";

        try {
            System.out.println("正在为用户 " + testUserId + " 读取数据并调用 DeepSeek API...");
            // 调用核心方法获取分析结果
            String analysisResult = client.getFinancialAnalysis(testUserId);

            System.out.println("\n--- DeepSeek API 原始响应 ---");
            System.out.println(analysisResult);
            System.out.println("--- 响应结束 ---");

            // 进阶：解析 JSON 获取更干净的分析文本
            // 你可以使用像 Jackson 或 Gson 这样的库来解析 JSON
            // 例如 (使用 Jackson):
            /*
            try {
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                com.fasterxml.jackson.databind.JsonNode rootNode = mapper.readTree(analysisResult);
                // 根据 DeepSeek 返回的 JSON 结构提取内容
                String content = rootNode.path("choices").get(0).path("message").path("content").asText();
                System.out.println("\n--- 提取的分析内容 ---");
                System.out.println(content);
            } catch (Exception jsonEx) {
                System.err.println("解析 JSON 响应时出错: " + jsonEx.getMessage());
            }
            */

        } catch (IOException e) {
            System.err.println("处理用户 " + testUserId + " 数据时出错: " + e.getMessage());
            // e.printStackTrace(); // 如果需要详细堆栈跟踪，取消注释此行
        } catch (InterruptedException e) {
            System.err.println("API 请求被中断: " + e.getMessage());
            Thread.currentThread().interrupt(); // 恢复中断状态
            // e.printStackTrace();
        } catch (RuntimeException e) {
            System.err.println("API 调用时发生运行时错误: " + e.getMessage());
            // e.printStackTrace();
        } catch (Exception e) { // 捕获其他未预料的异常
            System.err.println("发生未知错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}