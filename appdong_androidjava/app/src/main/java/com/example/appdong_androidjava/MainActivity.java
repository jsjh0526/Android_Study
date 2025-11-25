package com.example.appdong_androidjava;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText inputEditText;
    private RadioGroup conversionRadioGroup;
    private Button convertButton;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // XML 레이아웃을 Activity에 연결
        setContentView(R.layout.activity_main);

        // XML 레이아웃의 ID를 통해 View(UI 요소)들을 위에 선언한 변수에 대입
        inputEditText = findViewById(R.id.editText_input);
        conversionRadioGroup = findViewById(R.id.radioGroup_conversion_type);
        convertButton = findViewById(R.id.button_convert);
        resultTextView = findViewById(R.id.textView_result);

        // '변환' 버튼 클릭 리스너 설정
        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Conversion();
            }
        });
    }

    private void Conversion() {
        // 1. 입력 값 가져오기 및 유효성 검사
        String inputStr = inputEditText.getText().toString();
        if (inputStr.isEmpty()) {
            Toast.makeText(this, "변환할 숫자를 입력해 주세요.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        double inputValue;
        try {
            inputValue = Double.parseDouble(inputStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "유효한 숫자 형식으로 입력해 주세요.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. 선택된 변환 타입 확인
        int selectedId = conversionRadioGroup.getCheckedRadioButtonId();
        double resultValue = 0.0;
        String resultText = "";
        // 3. 변환 로직 수행
        if (selectedId == R.id.radio_inch_cm) {
            // 인치(in) -> 센티미터(cm) 변환 (1 in = 2.54 cm)
            resultValue = inputValue * 2.54;
            resultText = String.format("%.2f in 는 %.2f cm 입니다.", inputValue, resultValue);
        } else if (selectedId == R.id.radio_f_c) {
            // 화씨(°F) -> 섭씨(°C) 변환 (C = (F - 32) / 1.8)
            resultValue = (inputValue - 32) / 1.8;
            resultText = String.format("%.2f °F 는 %.2f °C 입니다.", inputValue, resultValue);
        } else if (selectedId == R.id.radio_pyeong_sqm) {
            // 평(P) -> 제곱미터(㎡) 변환 (1 평 ≈ 3.305785 ㎡)
            resultValue = inputValue * 3.305785;
            resultText = String.format("%.2f 평은 %.2f ㎡ 입니다.", inputValue, resultValue);
        } else {
            // 선택된 라디오 버튼이 없는 경우 (일반적으로 발생하지 않음)
            Toast.makeText(this, "변환 타입을 선택해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        // 4. 결과 출력
        resultTextView.setText(resultText);
    }
}