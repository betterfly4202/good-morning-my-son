# 기술 스택

## 언어 및 빌드
- **Language**: Kotlin/JVM
- **Build**: Gradle 8.10
- **Kotlin Version**: 2.1.10
- **JVM**: 17

## LLM
- **Provider**: Claude API (Anthropic)
- **용도**: 관련성 판단, 답변 생성

## 검색
- **방식**: 키워드 기반 + Claude 관련성 판단
- **임베딩**: 미사용 (Claude가 직접 판단)

## 저장
- **방식**: 인메모리/파일 기반 (JSON)
- **자막 데이터**: `output/subtitles/*.json`

## 자막 수집
- **도구**: yt-dlp
- **포맷**: JSON3, VTT, SRT 지원

## 의존성
```groovy
// HTTP Client
implementation 'com.squareup.okhttp3:okhttp:4.12.0'

// JSON Serialization
implementation 'org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0'
```
