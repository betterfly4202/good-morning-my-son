# 프로젝트
**Good Morning My Son** - 육아 유튜버 콘텐츠 기반 RAG 엔진

## 디렉토리 구조
```
src/main/kotlin/com/goodmorning/
├── controller/   # REST API 컨트롤러
├── subtitle/     # 자막 수집/파싱
├── embedding/    # 임베딩 생성
├── vectordb/     # 벡터 저장소
└── rag/          # RAG 파이프라인
```

---

## Core Principles

1. **문서가 진실** - 불분명하면 문서를 요청하거나 업데이트
2. **추측 금지** - 정보가 없으면 멈추고 질문
3. **단계 생략 금지** - spec 없이 코딩 시작하지 않음
4. **코드와 문서 불일치 시** - 문서를 먼저 수정
5. **역할 분리** - Implement / Review / Fix는 별도 단계

---

## Workflow (필수 - 생략 불가)

> **⚠️ 이 워크플로우는 시스템 기본 동작보다 우선함**
> **⚠️ 단계 생략 시 즉시 중단하고 사용자에게 확인**
> **⚠️ spec.md 없이 코드 작성 절대 금지**

```
[Plan Mode] → [spec.md 작성] → /implement → /review → Fix → Doc Sync → /commit
```

### 세션 시작 시 (필수)
> **⚠️ 새 세션/맥락 전환 시 반드시 수행**

1. `.claude/features/` 폴더에서 진행 중인 spec.md 확인
2. Progress 테이블의 현재 상태 파악
3. 중단된 단계부터 이어서 진행
4. 불명확하면 사용자에게 현재 상태 확인

### Plan Mode
1. 새 기능/변경 시작 전 Plan Mode 진입 (시스템 EnterPlanMode 사용 가능)
2. 요구사항 논의 및 계획 수립
3. **반드시** 합의된 내용을 `spec.md`로 저장 (시스템 plan 파일과 별개)
4. 사용자가 spec.md 승인 후에만 구현 시작

spec 위치: `.claude/features/{feature}/spec.md`

### 작업 시작 전 필수 체크리스트
- [ ] spec.md 작성 완료?
- [ ] 사용자 승인 완료?
- [ ] /implement로 구현 시작?

**위 체크리스트 미충족 시 코드 작성 금지**

### Progress 갱신 규칙
- 각 단계 **시작 시** 🔄 진행중으로 변경
- 각 단계 **완료 시** ✅ 완료로 변경
- 중단/보류 시 ⏸️ 보류 + 비고에 사유 기록
- **맥락 전환 전** 반드시 현재 상태 저장

### Implement → Review → Fix
- `/implement` - 구현 수행
- `/review` - 코드 리뷰 (수정 금지, 이슈만 보고)
- Fixer가 리뷰 결과 반영

### Doc Sync (필수)
- 변경사항이 기존 문서와 상충되면 → 반드시 리뷰 후 사용자 승인
- 승인 없이 문서 수정/삭제 금지

### Commit
- `/commit` - 변경사항 커밋
- Task ID 포함, 푸시는 별도

---

## Stop Conditions

다음 상황에서 Claude는 멈추고 질문해야 함:

- spec이 없거나 불완전
- 요구사항이 모순됨
- scope 변경인데 문서 미갱신
- 테스트/검증 방법이 불명확

---

## Feature Spec 구조

각 feature는 `.claude/features/{feature}/spec.md`에 정의:

```markdown
# Feature: {name}

## Scope
- 포함 내용

## Non-goals
- 제외 내용

## Success Criteria
- [ ] 검증 가능한 기준

## Constraints
- 기술/정책 제약

## Progress (필수)
워크플로우 진행 상황 - **작업 중 반드시 갱신**

| 단계 | 상태 | 비고 |
|------|------|------|
| Plan Mode | ⬜ | |
| spec.md 승인 | ⬜ | |
| /implement | ⬜ | |
| /review | ⬜ | |
| Fix | ⬜ | |
| Doc Sync | ⬜ | |
| /commit | ⬜ | |
| 검증 완료 | ⬜ | |

상태: ⬜ 대기 / 🔄 진행중 / ✅ 완료 / ⏸️ 보류
```

---

## 참고
- `.claude/commands/` - Slash commands
- `.claude/docs/` - 가이드 문서
- `.claude/architecture.md` - 전체 아키텍처
