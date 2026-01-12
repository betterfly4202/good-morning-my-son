# /task - 워크플로우 시작

새로운 태스크를 시작하고 feature branch와 spec.md를 생성합니다.

## 입력
- `[ISSUE-ID] 제목` 형식
- 예: `/task [ISSUE-1234] 새로운 기능 추가`

## 프로세스

```
┌─────────────────────────────────────────────────────────┐
│  1. 입력 파싱 ([ISSUE-ID] 추출)                         │
│  2. 구조화된 질문으로 요구사항 수집                     │
│  3. feature branch 생성                                 │
│  4. spec.md 생성                                        │
│  5. 승인 대기                                           │
└─────────────────────────────────────────────────────────┘
```

## 실행

### 1단계: 입력 파싱
$ARGUMENTS에서 다음을 추출:
- **ISSUE-ID**: 대괄호 안의 ID (예: `ISSUE-1234`)
- **제목**: 대괄호 뒤의 텍스트 (예: `새로운 기능 추가`)

ID는 소문자로 변환하여 사용 (예: `issue-1234`)

### 2단계: 구조화된 질문
사용자에게 다음 4가지를 순차적으로 질문:

```
이 태스크의 상세 내용을 알려주세요.

1. **Scope**: 이 기능에 포함할 내용을 설명해주세요.

2. **Non-goals**: 이 기능에서 제외할 내용이 있다면 설명해주세요.

3. **Success Criteria**: 어떤 조건이 충족되면 완료로 볼 수 있나요?

4. **Constraints**: 기술적/정책적 제약 사항이 있나요?
```

### 3단계: Branch 생성
사용자 응답 수신 후 **묻지 않고** 바로 실행:
```bash
git checkout -b feature/{issue-id}
```

### 4단계: Spec 생성
`.claude/features/{issue-id}/spec.md` 파일을 아래 템플릿으로 생성:

```markdown
# Feature: {제목}

## Issue
- {ISSUE-ID}

## Scope
{사용자가 입력한 Scope 내용}

## Non-goals
{사용자가 입력한 Non-goals 내용}

## Success Criteria
{사용자가 입력한 Success Criteria를 체크박스 형태로 변환}
- [ ] 기준 1
- [ ] 기준 2

## Constraints
{사용자가 입력한 Constraints 내용}

## Progress

| 단계 | 상태 | 비고 |
|------|------|------|
| Plan Mode | ✅ 완료 | /task로 생성 |
| spec.md 승인 | ⬜ 대기 | |
| implement-agent | ⬜ 대기 | |
| verify-agent | ⬜ 대기 | |
| review-agent | ⬜ 대기 | |
| Fix | ⬜ 대기 | |
| Doc Sync | ⬜ 대기 | |
| commit-agent | ⬜ 대기 | |

상태: ⬜ 대기 / 🔄 진행중 / ✅ 완료 / ⏸️ 보류
```

### 5단계: 승인 요청
spec.md 생성 후 사용자에게 확인 요청:
```
spec.md가 생성되었습니다: .claude/features/{issue-id}/spec.md

내용을 확인하고 승인해주세요. 승인 후 implement-agent로 구현을 시작합니다.
```

## 워크플로우 연결

> **⚠️ /task는 워크플로우의 시작점일 뿐, 전체 흐름을 생략할 수 없음**

```
/task → [spec.md 승인] → implement-agent → verify-agent → review-agent → Fix → Doc Sync → commit-agent
```

### 다음 단계
spec.md 승인 후 반드시 아래 순서로 진행:

| 순서 | Agent | 역할 | 생략 가능 |
|------|-------|------|----------|
| 1 | `implement-agent` | spec 기반 구현 | ❌ |
| 2 | `verify-agent` | Success Criteria 검증 | ❌ |
| 3 | `review-agent` | 코드 품질 리뷰 | ❌ |
| 4 | Fix | 리뷰 이슈 수정 | 이슈 없으면 생략 |
| 5 | Doc Sync | 문서 동기화 | 변경 없으면 생략 |
| 6 | `commit-agent` | 커밋 생성 | ❌ |

### 금지 사항
- **단계 생략 금지**: implement 후 바로 commit 불가
- **거짓 Progress 금지**: 실제 수행 없이 완료 표시 금지
- **순서 변경 금지**: verify 전에 review 불가

### Progress 마킹 규칙
- Progress 상태 변경은 **묻지 않고 바로 반영**
- 사용자에게 확인받을 것: **다음 단계로 넘어가도 되는지**만
- 다음 단계 승인 = 현재 단계 Progress 완료 마킹과 동일

## 규칙
- **자동 실행**: branch 생성과 spec 생성은 묻지 않고 바로 실행
- **소문자 변환**: ISSUE-ID는 branch/폴더명에서 소문자로 사용
- **템플릿 준수**: spec.md는 반드시 Progress 테이블 포함
- **승인 대기**: 구현은 사용자 승인 후 별도 시작

## 사용 예시
```
/task [FEAT-001] 사용자 인증 기능
/task [BUG-123] 로그인 오류 수정
/task [REFACTOR-45] API 구조 개선
```
