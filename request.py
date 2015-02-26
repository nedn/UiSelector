import httplib

def stop():
  conn = httplib.HTTPConnection('localhost', 9008)
  conn.request('PUT', '/stop')

stop()
conn = httplib.HTTPConnection('localhost', 9008)
res = conn.getresponse()
print res.status, res.reason
