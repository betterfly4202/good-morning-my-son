# Feature: Commands to Agents 전환

## Scope
- `.claude/commands/`의 워크플로우 관련 파일을 `.claude/agents/`로 이동
- 대상: implement, verify, review, commit
- agent 형식으로 문서 구조 변경

## Non-goals
- `summarize.md`는 도메인 기능이므로 commands에 유지
- agent 내용 자체의 변경 (구조만 전환)
- 기존 skills 폴더 (`nested-test-architect`) 변경

## 전환 대상

| 기존 (commands) | 신규 (agents) |
|-----------------|---------------|
| implement.md | implement-agent.md |
| verify.md | verify-agent.md |
| review.md | review-agent.md |
| commit.md | commit-agent.md |

## Agent 파일 구조
```markdown
---
name: {agent-name}
description: "{한 줄 설명}"
---

# {Agent Name}

{역할 정의 - "You are..." 형식}

## 프로세스
...

## 규칙
...
```

## Success Criteria
- [ ] `.claude/agents/` 디렉토리 생성
- [ ] 4개 agent 파일 생성 (implement, verify, review, commit)
- [ ] 기존 commands 파일 삭제 (4개)
- [ ] `summarize.md`는 commands에 유지
- [ ] CLAUDE.md 워크플로우 섹션 업데이트 (commands → agents 반영)
- [ ] prompt-management-guide.md 업데이트

## Constraints
- 기존 워크플로우 동작 방식 유지
- agent 내용은 기존 command 내용 기반으로 작성

## Progress

| 단계 | 상태 | 비고 |
|------|------|------|
| Plan Mode | ✅ 완료 | |
| spec.md 승인 | ✅ 완료 | |
| implement-agent | ✅ 완료 | |
| verify-agent | ✅ 완료 | |
| review-agent | ✅ 완료 | |
| Fix | ✅ 완료 | N/A (이슈 없음) |
| Doc Sync | ✅ 완료 | |
| commit-agent | ✅ 완료 | 7090691 |

상태: ⬜ 대기 / 🔄 진행중 / ✅ 완료 / ⏸️ 보류
