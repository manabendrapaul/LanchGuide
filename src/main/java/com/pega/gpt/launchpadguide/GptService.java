package com.pega.gpt.launchpadguide;

import com.opencsv.CSVWriter;
import com.opencsv.ICSVWriter;
import com.pega.gpt.launchpadguide.models.CsvRecord;
import com.pega.gpt.launchpadguide.models.Result;
import com.pega.gpt.launchpadguide.models.SimilarityRecord;
import com.pega.gpt.launchpadguide.service.CSVHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON;


@Slf4j
@RequiredArgsConstructor
@Service
public class GptService {

    @Value("${openai.token}")
    private String openAiToken;

    @Value("${openai.embeddings.url}")
    private String openEmbeddingsUrl;

    @Value("${output.csv.path}")
    private String outputPath;

    @Value("${python.path}")
    private String pythonPath;

    @Value("${cosine.script.path}")
    private String cosineScriptPath;

    public void save(MultipartFile file) {
        try {
            List<CsvRecord> csvRecords = CSVHelper.readDataLoadCsv(file.getInputStream());
            List<String[]> embeddingsArray = csvRecords.stream().filter(p -> StringUtils.isNotBlank(p.getConcatenatedString())).map(p -> new String[]{p.getConcatenatedString(), getEmbeddings(p.getConcatenatedString())}).collect(Collectors.toList());
            writeToCsv(embeddingsArray);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Failed to store csv data: " + e.getMessage());
        }
    }

    private void writeToCsv(List<String[]> data) throws IOException {
        try (Writer fileWriter = Files.newBufferedWriter(Paths.get(outputPath))) {
            CSVWriter csvWriter =
                    new CSVWriter(
                            fileWriter,
                            ICSVWriter.DEFAULT_SEPARATOR,
                            ICSVWriter.DEFAULT_QUOTE_CHARACTER,
                            ICSVWriter.DEFAULT_ESCAPE_CHARACTER,
                            ICSVWriter.DEFAULT_LINE_END);
            csvWriter.writeNext(new String[]{"Text", "Embedding"});
            csvWriter.writeAll(data);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new IOException(e);
        }
    }

    public String getEmbeddings(String input) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(openAiToken);
            headers.setContentType(APPLICATION_JSON);
            HttpEntity entity = new HttpEntity(Map.of("input", input, "model", "text-embedding-ada-002"), headers);
            ResponseEntity<Result> result = restTemplate.postForEntity(openEmbeddingsUrl, entity, Result.class);
            log.info("ChatGPT Response: {}", result);
            return Objects.requireNonNull(result.getBody()).getData().get(0).getEmbedding().stream().map(String::valueOf).collect(Collectors.joining(","));
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return null;
    }

    public List<String> getSuggestions(Integer count, String text) throws IOException, InterruptedException {
        int exitCode = triggerPythonScript(text);
        if (exitCode == 0) {
            List<SimilarityRecord> similarityRecords = CSVHelper.readSimilarities(new FileInputStream(outputPath));
            int limit = Objects.nonNull(count) ? count > similarityRecords.size() ? similarityRecords.size() : count : 10;
            return similarityRecords.stream().sorted(Comparator.comparing(SimilarityRecord::getSimilarities).reversed()).limit(limit).map(SimilarityRecord::getText).collect(Collectors.toList());
        }
        throw new IllegalArgumentException("Some error occurred");
    }

    private int triggerPythonScript(String text) throws IOException, InterruptedException {
        return Runtime.getRuntime().exec(new String[]{pythonPath, cosineScriptPath, openAiToken, text, outputPath}).waitFor();
    }

}
