# /script/transcript.py
import sys
import json
from youtube_transcript_api import YouTubeTranscriptApi

try:
    video_id = sys.argv[1]
    transcript = YouTubeTranscriptApi.get_transcript(video_id, languages=["ko", "en"])
    print(json.dumps(transcript, ensure_ascii=False))
except Exception as e:
    print(f"❌ Error: {e}")
    sys.exit(1)
