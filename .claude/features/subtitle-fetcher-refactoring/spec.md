# Feature: YouTubeSubtitleFetcher 리팩토링

## Scope

`YouTubeSubtitleFetcher`를 Code Constitution에 따라 단일 책임 컴포넌트들로 분리

### 포함
- 기존 클래스를 6개 컴포넌트로 분리
- 인터페이스 도입으로 테스트 가능한 구조
- SLF4J 로깅 적용
- 단위 테스트 작성

### 분리할 컴포넌트
| 컴포넌트 | 책임 |
|----------|------|
| `YtDlpClient` (interface) | yt-dlp 실행 추상화 |
| `ProcessBasedYtDlpClient` | 실제 프로세스 실행 |
| `SubtitleFileFinder` | 다운로드된 파일 탐색 |
| `SubtitleParser` (interface) | 자막 파싱 추상화 |
| `Json3SubtitleParser` | JSON3 형식 파싱 |
| `VttSubtitleParser` | VTT 형식 파싱 |
| `SrtSubtitleParser` | SRT 형식 파싱 |
| `SubtitleFetchPipeline` | 컴포넌트 조합 (오케스트레이터) |

## Non-goals

- 새로운 자막 포맷 지원 추가
- API 응답 구조 변경
- 캐싱 기능 추가
- 비동기 처리 도입

## Success Criteria

- [ ] `./gradlew build` 성공
- [ ] `./gradlew test` 전체 통과
- [ ] 각 파서별 최소 2개 테스트 케이스
- [ ] 기존 API 동작 유지 (`GET /api/subtitles/{videoId}`)
- [ ] println 제거, SLF4J Logger 사용
- [ ] 각 컴포넌트 100줄 이하
- [ ] 기존 `YouTubeSubtitleFetcher.kt` 삭제

## Constraints

- Spring Boot 3.4.1 / Kotlin 유지
- 기존 `SubtitleController` 호환성 유지
- kotlinx.serialization 사용 유지

## 디렉토리 구조 (변경 후)

```
src/main/kotlin/com/goodmorning/subtitle/
├── model/
│   └── Subtitle.kt                  # 기존 유지
├── client/
│   ├── YtDlpClient.kt               # 신규 (인터페이스)
│   ├── YtDlpResult.kt               # 신규 (결과 모델)
│   └── ProcessBasedYtDlpClient.kt   # 신규 (구현체)
├── parser/
│   ├── SubtitleParser.kt            # 신규 (인터페이스)
│   ├── Json3SubtitleParser.kt       # 신규
│   ├── VttSubtitleParser.kt         # 신규
│   └── SrtSubtitleParser.kt         # 신규
├── SubtitleFileFinder.kt            # 신규
└── SubtitleFetchPipeline.kt         # 신규 (오케스트레이터)

src/test/kotlin/com/goodmorning/subtitle/
├── parser/
│   ├── Json3SubtitleParserTest.kt
│   ├── VttSubtitleParserTest.kt
│   └── SrtSubtitleParserTest.kt
└── SubtitleFileFInderTest.kt
```

## 구현 순서

### Phase 1: 인터페이스 정의
1. `YtDlpResult.kt` - 결과 모델
2. `YtDlpClient.kt` - 인터페이스
3. `SubtitleParser.kt` - 인터페이스

### Phase 2: 파서 구현 + 테스트
4. `Json3SubtitleParser.kt` + 테스트
5. `VttSubtitleParser.kt` + 테스트
6. `SrtSubtitleParser.kt` + 테스트

### Phase 3: 인프라 컴포넌트
7. `ProcessBasedYtDlpClient.kt`
8. `SubtitleFileFinder.kt` + 테스트

### Phase 4: 파이프라인 통합
9. `SubtitleFetchPipeline.kt`
10. `SubtitleController.kt` 수정 (의존성 변경)
11. 통합 테스트

### Phase 5: 정리
12. `YouTubeSubtitleFetcher.kt` 삭제
13. spec.md Progress 업데이트

## 검증 방법

```bash
# 1. 빌드 확인
./gradlew build

# 2. 테스트 실행
./gradlew test

# 3. 서버 기동 후 API 테스트
./gradlew bootRun
curl http://localhost:8080/api/subtitles/{video-id}
```

## Code Constitution 준수 확인

| Rule | 적용 |
|------|------|
| Rule 14 (단일 책임) | 각 컴포넌트 1개 책임만 담당 |
| Rule 15 (파이프라인) | SubtitleFetchPipeline으로 조합 |
| Rule 16 (교체 가능) | 인터페이스로 구현체 교체 가능 |
| Rule 17 (관찰 가능성) | SLF4J Logger 사용 |
| Rule 19 (테스트 가능) | Mock 주입 가능한 구조 |

## 수정 대상 파일

| 파일 | 작업 |
|------|------|
| `YouTubeSubtitleFetcher.kt` | 삭제 |
| `SubtitleController.kt` | 의존성 변경 |
| 신규 8개 파일 | 생성 |
| 테스트 4개 파일 | 생성 |

## Progress

| 단계 | 상태 | 비고 |
|------|------|------|
| Plan Mode | ✅ 완료 | |
| spec.md 승인 | ✅ 완료 | |
| /implement | ✅ 완료 | |
| /verify | ✅ 완료 | 모든 Success Criteria 충족 |
| /review | ✅ 완료 | PASS with notes (Medium 2개, Non-blocking) |
| Fix | ✅ 완료 | High 이슈 없어 수정 불필요 |
| Doc Sync | ✅ 완료 | architecture.md 변경 불필요, 테스트 @Nested 구조 리팩토링 |
| /commit | ✅ 완료 | 634d71d |

상태: ⬜ 대기 / 🔄 진행중 / ✅ 완료 / ⏸️ 보류
