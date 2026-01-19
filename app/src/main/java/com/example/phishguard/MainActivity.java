package com.example.phishguard;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText smsInput;
    private Button analyzeButton;
    private TextView resultText;

    // Dolandırıcılık kelimeleri havuzu
    private final String[] suspiciousWords = {
        "hesap", "banka", "şifre", "güncelleme", "doğrulama", "tıkla",
        "acil", "ödeme", "para", "çekiliş", "kazandınız", "ücretsiz",
        "indirim", "fırsat", "kredi", "borç", "verg", "vergi",
        "polis", "savcılık", "mahkeme", "yasak", "engelli", "blok"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI elemanlarını başlat
        smsInput = findViewById(R.id.sms_input);
        analyzeButton = findViewById(R.id.analyze_button);
        resultText = findViewById(R.id.result_text);

        // Analiz butonuna tıklama olayı
        analyzeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                analyzeSMS();
            }
        });
    }

    private void analyzeSMS() {
        String smsText = smsInput.getText().toString().toLowerCase().trim();

        if (smsText.isEmpty()) {
            resultText.setText("Lütfen bir SMS metni girin!");
            resultText.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            return;
        }

        boolean isSuspicious = checkForSuspiciousContent(smsText);

        if (isSuspicious) {
            resultText.setText(getString(R.string.phishing_message));
            resultText.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        } else {
            resultText.setText(getString(R.string.safe_message));
            resultText.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        }
    }

    private boolean checkForSuspiciousContent(String text) {
        // Şüpheli kelimelerin sayısını kontrol et
        int suspiciousWordCount = 0;

        for (String word : suspiciousWords) {
            if (text.contains(word)) {
                suspiciousWordCount++;
            }
        }

        // Büyük harf kullanımı kontrolü
        boolean hasAllCaps = text.equals(text.toUpperCase()) && text.length() > 10;

        // Ünlem işareti fazlalığı kontrolü
        long exclamationCount = text.chars().filter(ch -> ch == '!').count();
        boolean hasTooManyExclamations = exclamationCount > 3;

        // Noktalama işareti fazlalığı kontrolü
        long questionMarkCount = text.chars().filter(ch -> ch == '?').count();
        boolean hasTooManyQuestions = questionMarkCount > 2;

        // Eğer şüpheli kelime sayısı 2'den fazla veya diğer kriterler varsa şüpheli olarak işaretle
        return suspiciousWordCount >= 2 || hasAllCaps || hasTooManyExclamations || hasTooManyQuestions;
    }
}