import sys
import json
from youtube_transcript_api import YouTubeTranscriptApi
from sentence_splitter import SentenceSplitter

def transcript_chunked(video_id: str):
    try:
        transcript = YouTubeTranscriptApi.get_transcript(video_id, languages=["ko", "en"])
        full_text = " ".join([item["text"] for item in transcript])

        splitter = SentenceSplitter(language='en')
        sentences = splitter.split(full_text)

        result = [{"text": sentence.strip()} for sentence in sentences]
        print(json.dumps(result, ensure_ascii=False))
    except Exception as e:
        print(json.dumps({"error": str(e)}, ensure_ascii=False))

if __name__ == "__main__":
    video_id = sys.argv[1]
    transcript_chunked(video_id)