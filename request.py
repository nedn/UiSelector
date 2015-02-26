import httplib
import cProfile

def stop():
  conn = httplib.HTTPConnection('localhost', 9008)
  conn.request('GET', '/stop')

def SelectChromeMenu(print_results=False):
  conn = httplib.HTTPConnection('localhost', 9008)
  HEADERS = {
      'resource_id' : 'com.android.chrome:id/document_menu_button'
  }
  conn.request('GET', '/select', headers=HEADERS)
  res = conn.getresponse()
  data = res.read()
  if print_results:
    print data

if __name__ == '__main__':
  for _ in range(100):
    SelectChromeMenu()
