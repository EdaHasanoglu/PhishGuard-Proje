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

    // Sosyal mühendislik kelimeleri havuzu
    private final String[] socialEngineeringWords = {
        "acil", "hemen", "hesabınız silinecek", "tebrikler", "şifre",
        "hızlı", "anında", "derhal", "acele", "kritik", "önemli",
        "dikkat", "uyarı", "tehlike", "risk", "zarar", "kaybetme",
        "kaçırma", "fırsat", "şans", "ödül", "para", "nakit"
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

        boolean isSocialEngineering = checkForSocialEngineering(smsText);

        if (isSocialEngineering) {
            resultText.setText("TEHLİKE: Sosyal Mühendislik Şüphesi! (Puan: %80)");
            resultText.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        } else {
            resultText.setText("GÜVENLİ: Manipülasyon tespit edilemedi");
            resultText.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        }
    }

    private boolean checkForSocialEngineering(String text) {
        // Sosyal mühendislik kelimelerini kontrol et
        for (String word : socialEngineeringWords) {
            if (text.contains(word)) {
                return true; // Herhangi bir sosyal mühendislik kelimesi bulunursa true döndür
            }
        }

        return false; // Hiç sosyal mühendislik kelimesi bulunmadıysa false döndür
    }
}