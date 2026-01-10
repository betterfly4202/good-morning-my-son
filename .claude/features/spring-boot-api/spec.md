# Feature: Spring Boot API Controller

## Scope
- 기존 Kotlin 프로젝트를 Spring Boot 3.4.1 기반으로 전환
- `YouTubeSubtitleFetcher`를 호출하는 REST API Controller 추가
- API 엔드포인트: `GET /api/subtitles/{videoId}`
- 기존 CLI(`Main.kt`) 제거 → Spring Boot 애플리케이션으로 통합

## Non-goals
- 기존 `YouTubeSubtitleFetcher` 로직 변경
- 데이터베이스 연동
- 인증/인가

## API 설계

### GET /api/subtitles/{videoId}
- **Request**: Path parameter `videoId` (YouTube 영상 ID)
- **Response (200)**: `VideoSubtitle` JSON
- **Response (404)**: 자막을 찾을 수 없음

```json
{
  "videoId": "abc123",
  "title": "영상 제목",
  "channelName": "하정훈의 삐뽀삐뽀 119 소아과",
  "collectedAt": "2026-01-11T...",
  "segments": [...],
  "fullText": "전체 자막 텍스트"
}
```

## 변경 파일

| 파일 | 작업 |
|------|------|
| `build.gradle` | Spring Boot 플러그인 및 의존성 추가, application 블록 제거 |
| `Main.kt` | 삭제 |
| `GoodMorningApplication.kt` | 신규 - Spring Boot Application |
| `controller/SubtitleController.kt` | 신규 - REST Controller |
| `application.yml` | 신규 - 서버 설정 |
| `CLAUDE.md` | 디렉토리 구조에 `controller/` 추가 |

## Success Criteria
- [ ] `./gradlew build` 성공
- [ ] `./gradlew bootRun`으로 서버 기동
- [ ] `curl http://localhost:8080/api/subtitles/{videoId}` 호출 시 JSON 응답
- [ ] 자막 없는 경우 404 응답

## Constraints
- Spring Boot 3.4.1 (최신 안정 버전)
- Kotlin 2.1.10 유지
- JDK 17 유지
- `channelName` 기본값 유지: "하정훈의 삐뽀삐뽀 119 소아과"

---

## Progress

| 단계 | 상태 | 비고 |
|------|------|------|
| Plan Mode | ✅ | 요구사항 논의 완료 |
| spec.md 승인 | ✅ | Main.kt 삭제 포함 |
| /implement | ✅ | 빌드 성공 |
| /review | ⬜ | |
| Fix | ⬜ | |
| Doc Sync | ⬜ | |
| /commit | ⬜ | |
| 검증 완료 | ⬜ | bootRun, curl 테스트 필요 |

상태: ⬜ 대기 / 🔄 진행중 / ✅ 완료 / ⏸️ 보류
