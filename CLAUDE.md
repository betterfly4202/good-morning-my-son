# 프로젝트
**Good Morning My Son** - 육아 유튜버 콘텐츠 기반 RAG 엔진

## 디렉토리 구조
```
src/main/kotlin/com/goodmorning/
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

## Workflow

```
[Plan Mode] → [Implement] → [Review] → [Fix] → [Doc Sync] → [Commit]
```

### Plan Mode (Shift+Tab 2번)
1. 새 기능/변경 시작 전 Plan Mode 진입
2. 요구사항 논의 및 계획 수립
3. 합의된 내용을 `spec.md`로 저장 (히스토리 기록)
4. spec 확정 후 구현 시작

spec 위치: `.claude/features/{feature}/spec.md`

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
```

---

## 참고
- `.claude/commands/` - Slash commands
- `.claude/agents/` - Sub-agents
- `.claude/architecture.md` - 전체 아키텍처
