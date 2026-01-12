# Feature: /task 커맨드

## Scope
- `/task [ISSUE-ID] 제목` 형식의 커맨드 생성
- 구조화된 질문으로 상세 요구사항 수집 (Scope, Non-goals, Success Criteria, Constraints)
- `feature/{issue-id}` 형식의 브랜치 자동 생성
- `.claude/features/{issue-id}/spec.md` 자동 생성
- 수집한 정보를 spec 템플릿에 반영

## Non-goals
- 브랜치 푸시 (사용자가 별도로 수행)
- spec 승인 후 자동 구현 시작
- GitHub 이슈 연동/조회

## Success Criteria
- [x] `.claude/commands/task.md` 파일 생성
- [x] `[ISSUE-ID]` 형식 파싱 기능
- [x] 4가지 구조화된 질문 (Scope, Non-goals, Success Criteria, Constraints)
- [x] `feature/{issue-id}` 브랜치 생성 명령어 포함
- [x] spec.md 템플릿 포함 (Progress 테이블 포함)
- [x] 워크플로우 연결 명시 (task.md 내 다음 단계 안내)
- [x] CLAUDE.md에 /task 커맨드 흐름 반영

## Constraints
- 기존 commands 형식 준수 (summarize.md 참고)
- CLAUDE.md 워크플로우와 연동 가능해야 함

## Progress

| 단계 | 상태 | 비고 |
|------|------|------|
| Plan Mode | ✅ 완료 | |
| spec.md 승인 | ✅ 완료 | |
| implement-agent | ✅ 완료 | |
| verify-agent | ✅ 완료 | 모든 Criteria Pass |
| review-agent | ✅ 완료 | PASS, 이슈 없음 |
| Fix | ✅ 완료 | N/A (이슈 없음) |
| Doc Sync | ✅ 완료 | CLAUDE.md 갱신 |
| commit-agent | ⬜ 대기 | |

상태: ⬜ 대기 / 🔄 진행중 / ✅ 완료 / ⏸️ 보류
