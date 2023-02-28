package com.pega.gpt.launchpadguide.controller;

import com.pega.gpt.launchpadguide.GptService;
import com.pega.gpt.launchpadguide.service.CSVHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gpt")
@RequiredArgsConstructor
@Slf4j
public class GptController {

    private final GptService gptService;

    @PostMapping("/loadEmbeddings")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        if (CSVHelper.hasCSVFormat(file)) {
            try {
                gptService.save(file);
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Uploaded the file successfully: " + file.getOriginalFilename()));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(Map.of("message", "Could not upload the file: " + file.getOriginalFilename() + "!"));
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Please upload a .csv file!"));
    }

    @GetMapping("/suggestions/count/{count}")
    public ResponseEntity<Map<String, List<String>>> getSuggestions(@PathVariable Integer count, @RequestParam String text) throws IOException, InterruptedException {
        return ResponseEntity.ok(Map.of("data", gptService.getSuggestions(count, text)));
    }

}
