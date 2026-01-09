# 개발 진행 로그

## 2026-01-10

### 완료
- YouTube 자막 수집기 구현 (Phase 1 첫 번째 작업)
  - yt-dlp 기반 자막 추출
  - JSON3 포맷 파싱
  - 영상 메타데이터 수집

### 구현된 파일
- `src/main/kotlin/com/goodmorning/subtitle/YouTubeSubtitleFetcher.kt`
- `src/main/kotlin/com/goodmorning/subtitle/SubtitleParser.kt`
- `src/main/kotlin/com/goodmorning/subtitle/model/Subtitle.kt`
- `src/main/kotlin/Main.kt`

### 사용법
```bash
# 단일 영상
./gradlew run --args="VIDEO_ID"

# 여러 영상
./gradlew run --args="VIDEO_ID1 VIDEO_ID2"

# 파일에서 영상 ID 읽기
./gradlew run --args="--file video_ids.txt"
```

### 출력
- 위치: `output/subtitles/VIDEO_ID.json`
- 포맷: JSON (videoId, title, channelName, collectedAt, segments, fullText)

### 결정 사항
- **자막 수집 도구**: yt-dlp (YouTube API 직접 접근 제한으로 인해 변경)
- **대상 유튜버**: 하정훈의 삐뽀삐뽀 119 소아과
- **개발 접근**: End-to-End 프로토타입 먼저 (1개 영상 → 전체 흐름 검증 → 데이터 확장)
- **검색 방식**: 키워드 기반 + Claude 관련성 판단 (임베딩 API 미사용)
- **저장 방식**: 인메모리/파일 기반 JSON

### Git 형상관리
- `834a9d5` docs: 프로젝트 요구사항 및 작업계획서 정의
- `1dcb828` feat: Phase 1 YouTube 자막 수집기 구현

### 다음 작업
- [ ] End-to-End 프로토타입 구현
  - 청크 분할
  - 키워드 검색
  - Claude 관련성 판단
  - 답변 생성/보류

---

## 2026-01-09

### 완료
- 프로젝트 작업계획서 리뷰
- RAG 시스템 아키텍처 결정 (Claude API 활용)
- CLAUDE.md 업데이트 - 프로젝트 맥락 추가
- 문서 관리 체계 수립
- 워크플로우 정의

### 결정 사항
- **LLM**: Claude API 사용 (자체 LLM 구축 X)
- **아키텍처**: RAG (Retrieval-Augmented Generation)
- **문서 관리**: CLAUDE.md + docs/progress.md 이원화
