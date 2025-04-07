import sys
import json
import logging
from sentence_transformers import SentenceTransformer

# 로그 무력화
logging.getLogger().setLevel(logging.ERROR)

model = SentenceTransformer('sentence-transformers/all-MiniLM-L6-v2')

def main():
    input_json = sys.stdin.read()
    try:
        chunks = json.loads(input_json)
        embeddings = model.encode(chunks).tolist()
        result = [{"text": chunk, "embedding": vector} for chunk, vector in zip(chunks, embeddings)]
        print(json.dumps(result, ensure_ascii=False))
    except Exception as e:
        # 에러는 stderr로
        print(json.dumps({"error": str(e)}), file=sys.stderr)

if __name__ == "__main__":
    main()
