# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

---

## 프로젝트: Good Morning My Son

### 목표
특정 육아 유튜버의 콘텐츠만을 기반으로 답변하는 RAG 엔진 구축

### 핵심 철학
> "AI가 똑똑해 보이는 것보다, 정직하게 모른다고 말하는 것이 더 중요하다"

- 근거 없는 답변 금지
- 단일 전문가 기반 답변
- 일반 육아 상식 혼합 금지

### 기술 스택
- **Language**: Kotlin/JVM
- **LLM**: Claude API (Anthropic)
- **Vector DB**: TBD
- **Build**: Gradle 8.10

---

## 개발 Phase

### Phase 1: 데이터 파이프라인 (현재)
- [x] 유튜브 자막 수집기 (yt-dlp 기반)
- [ ] 스크립트 정제기
- [ ] 메타데이터 태깅

### Phase 2: 검색 시스템
- [ ] 임베딩 생성
- [ ] 벡터 DB 저장
- [ ] 유사도 검색

### Phase 3: 답변 엔진
- [ ] 질문 정제
- [ ] 유사도 판단
- [ ] 답변 생성/보류

---

## 현재 상태
- **Phase**: 1 - 데이터 파이프라인
- **대상 유튜버**: 하정훈의 삐뽀삐뽀 119 소아과 (UC6t0ees15Lp0gyrLrAyLeJQ)
- **완료**: 자막 수집기 구현 (yt-dlp 기반)
- **다음 작업**: 스크립트 정제기 구현

---

## 참고 문서
- `project-plan.md` - 전체 작업계획서
- `docs/progress.md` - 상세 진행 로그

---

## 워크플로우

### 작업 흐름
```
1. 작업 시작
   - CLAUDE.md에서 현재 상태 확인
   - 오늘 할 작업 결정

2. 계획 수립
   - 복잡한 작업 → Plan Mode로 설계 먼저
   - 간단한 작업 → 바로 TodoWrite

3. 구현 (TodoWrite 추적)
   - 세부 작업 분해
   - 완료할 때마다 체크

4. 마무리
   - progress.md에 결과 기록
   - CLAUDE.md 상태 업데이트
   - 컨텍스트 길면 /compact
```

### 도구 활용
| 레벨 | 도구 | 용도 |
|------|------|------|
| 프로젝트 | CLAUDE.md | 전체 Phase 진행 상태 |
| 일간/세션 | docs/progress.md | 결정사항, 완료 작업 기록 |
| 작업 단위 | TodoWrite | 실시간 작업 추적 |
| 설계 | Plan Mode | 복잡한 기능 구현 전 설계 |

### Sub-Agent 활용
| Agent | 활용 시점 |
|-------|-----------|
| Explore | 코드베이스 탐색, "이 기능 어디 있어?" |
| Plan | 큰 기능 구현 전 설계, 아키텍처 결정 |
| Bash | Git 작업, 빌드, 테스트 실행 |

---
# 개인 설정
## 개인 작업 선호사항 및 코칭 스타일
- 작업 전 유의사항
    - 스스로 전체 구현을 시작하지말고, 참고할만한 레퍼런스를 물어봐라
    - 외부 의존성 추가는 절대 금물. 반드시 필요하면 나한테 꼭 물어보고 진행해

## 코드 작성 규칙
- SOLID 원칙 준수
- 디자인 패턴에 대한 팁을 고려하기
- 새로운 의존성을 추가하는 것은 최대한 지양하기
- 클린 코드 원칙 적용

