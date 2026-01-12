# Feature: 문단 단위 청크 분할

## Issue
- ISSUE-3

## Scope
수집된 자막(`VideoSubtitle`)을 RAG 검색에 적합한 청크 단위로 분할하는 기능 구현.

### 포함 내용
- **청크 데이터 모델**: `SubtitleChunk` 정의 (텍스트, 시간 범위, 원본 세그먼트 참조)
- **청크 분할 전략 인터페이스**: `ChunkingStrategy` (Strategy 패턴)
- **시간 기반 분할 전략**: 일정 시간 간격(예: 30초)으로 세그먼트 묶기
- **크기 기반 분할 전략**: 목표 글자 수(예: 500자) 기준 분할
- **파이프라인 통합**: `SubtitleFetchPipeline` 결과를 청크로 변환

### 청크 설계 원칙
- RAG 적정 크기: 300~800자 (설정 가능)
- 문장 중간 절단 최소화
- 청크 간 오버랩 옵션 (맥락 유지)

## Non-goals
- 의미 기반 분할 (LLM 활용한 주제 감지)
- 메타데이터 부여 (영상 제목, 태그 등) - 다음 태스크
- 청크 저장소 구현 - Phase 2
- 임베딩 생성 - Phase 2

## Success Criteria
- [x] `SubtitleChunk` 데이터 모델 정의
- [x] `ChunkingStrategy` 인터페이스 정의
- [x] 시간 기반 분할 전략 구현 및 테스트
- [x] 크기 기반 분할 전략 구현 및 테스트
- [x] `VideoSubtitle` → `List<SubtitleChunk>` 변환 동작 확인
- [x] 기존 필터 패턴과 일관된 Strategy 패턴 적용

## Constraints
- 기존 `subtitle` 패키지 구조 유지 (`subtitle/chunker/` 하위에 생성)
- 기존 코드의 Strategy 패턴과 일관성 유지
- Kotlin 컨벤션 준수
- 단위 테스트 필수

## Technical Design

### 데이터 모델
```kotlin
data class SubtitleChunk(
    val text: String,           // 청크 텍스트
    val startTime: Double,      // 시작 시간(초)
    val endTime: Double,        // 종료 시간(초)
    val segmentCount: Int       // 포함된 세그먼트 수
)
```

### 패키지 구조
```
subtitle/
└── chunker/
    ├── SubtitleChunk.kt           # 데이터 모델
    ├── ChunkingStrategy.kt        # 인터페이스
    ├── TimeBasedChunkingStrategy.kt    # 시간 기반
    └── SizeBasedChunkingStrategy.kt    # 크기 기반
```

## Progress

| 단계 | 상태 | 비고 |
|------|------|------|
| Plan Mode | ✅ 완료 | /task로 생성 |
| spec.md 승인 | ✅ 완료 | |
| implement-agent | ✅ 완료 | 빌드/테스트 통과 |
| verify-agent | ✅ 완료 | 모든 Success Criteria 충족 |
| review-agent | ✅ 완료 | 1단계/2단계 모두 PASS |
| Fix | ✅ 완료 | 오버랩 세그먼트 구조 유지로 수정 |
| Doc Sync | ✅ 완료 | architecture.md 업데이트 |
| commit-agent | ✅ 완료 | 2개 커밋 생성 |

상태: ⬜ 대기 / 🔄 진행중 / ✅ 완료 / ⏸️ 보류

## Review History

### Review #1 - 2026-01-12 (무효 - Codex 위임 누락)
**Verdict**: ❌ FAIL (절차 오류로 무효 처리)

**비고**: Codex 위임 없이 직접 리뷰 수행됨. review-agent 지침 위반으로 무효.

**발견된 이슈 (참고용)**:
| Severity | File | Issue | Status |
|----------|------|-------|--------|
| High | TimeBasedChunkingStrategy.kt:18-62 | Mutable state 사용 (RH-1, RH-2 위반) | ⬜ 대기 |
| High | SizeBasedChunkingStrategy.kt:19-78 | Mutable state 사용 (RH-1, RH-2 위반) | ⬜ 대기 |
| High | 전략 클래스 전체 | Observability 부재 (OP-1 위반) | ⬜ 대기 |
| Medium | SizeBasedChunkingStrategy.kt:50 | 오버랩 시 segmentCount 계산 오류 | ⬜ 보류 |
| Medium | SizeBasedChunkingStrategy.kt:60 | 길이 계산 로직 불일치 | ⬜ 보류 |
| Low | SubtitleChunk.kt:6-11 | 데이터 모델 불변성 검증 부재 | ⬜ 보류 |

**Fix Actions (보류)**:
- [ ] Codex 리뷰 후 재확인 필요

---

### Review #2 - 2026-01-13

**1단계 (기능/의도)**: ❌ FAIL

| 문제 | 파일 | 수정 내용 |
|------|------|-----------|
| 오버랩 시 세그먼트 구조 손실 | SizeBasedChunkingStrategy.kt:52-67 | 세그먼트 단위 오버랩으로 변경 |

**상세 설명**:
- 기존: 오버랩 텍스트를 글자 단위로 잘라서 단일 문자열로 저장
- 문제: segmentCount=1로 고정, startTime이 현재 세그먼트로 덮어씀
- 수정: 마지막 N개 세그먼트를 구조 유지하며 다음 청크로 전달

**2단계 (설계/구조)**: ✅ PASS

| Severity | File | Issue | Status |
|----------|------|-------|--------|
| Medium | SizeBasedChunkingStrategy.kt | OP-2: 파라미터 계약 미명시 | ✅ 해결 |

**수정 내용**:
- `require()` 검증 추가 (targetSize > 0, overlapSegments >= 0)
- KDoc에 동작 규칙 명시
- 엣지 케이스 테스트 추가 (파라미터 검증, 단일 세그먼트 초과, 오버랩 초과)

**Fix Actions**:
- [x] 오버랩을 세그먼트 단위로 변경 (overlapSegments 방식)
- [x] 테스트 케이스 수정 (세그먼트 구조 유지 검증)

**수정 내용**:
- `overlapSize` (글자 수) → `overlapSegments` (세그먼트 개수) 변경
- `currentChunkSegments: List<String>` → `currentSegments: List<SubtitleSegment>` 변경
- 오버랩 시 마지막 N개 세그먼트를 구조 그대로 유지
- startTime, endTime, segmentCount가 실제 세그먼트 기반으로 정확히 계산됨

---
