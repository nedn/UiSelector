import httplib
import cProfile

def stop():
  conn = httplib.HTTPConnection('localhost', 9008)
  conn.request('GET', '/stop')

def SelectCorpusBar():
  conn = httplib.HTTPConnection('localhost', 9008)
  HEADERS = {
  'resource_id' : 'com.google.android.googlequicksearchbox:id/corpus_bar'
  }
  conn.request('GET', '/select', headers=HEADERS)
  res = conn.getresponse()
  data = res.read()

if __name__ == '__main__':
  for _ in range(100):
    SelectCorpusBar()
