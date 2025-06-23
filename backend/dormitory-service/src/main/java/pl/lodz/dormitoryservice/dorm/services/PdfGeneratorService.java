package pl.lodz.dormitoryservice.dorm.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;
import pl.lodz.dormitoryservice.dorm.DTO.DormFormCreateDTO;

import java.io.*;

@Service
public class PdfGeneratorService {

    public String generatePdfBase64FromForm(DormFormCreateDTO dto) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            String json = mapper.writeValueAsString(dto);

            ProcessBuilder pb = new ProcessBuilder(
                    "./.venv/Scripts/python.exe",
                    "./dormitory-service/src/main/resources/pl/lodz/dormitoryservice/scripts/generate_pdf.py"
            );
            Process process = pb.start();

            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
                writer.write(json);
                writer.flush();
                process.getOutputStream().close();
            }

            StringBuilder output = new StringBuilder();
            StringBuilder error = new StringBuilder();

            Thread outputThread = new Thread(() -> readStream(process.getInputStream(), output));
            Thread errorThread = new Thread(() -> readStream(process.getErrorStream(), error));

            outputThread.start();
            errorThread.start();

            int exitCode = process.waitFor();

            outputThread.join();
            errorThread.join();

            if (exitCode != 0) {
                throw new RuntimeException("PDF generation failed: " + error.toString());
            }

            return output.toString().trim();

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF: " + e.getMessage(), e);
        }
    }

    private void readStream(InputStream stream, StringBuilder output) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading stream: " + e.getMessage(), e);
        }
    }
}
