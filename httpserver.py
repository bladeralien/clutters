import socket

host, port = '', 50000
backlog = 5 
size = 1024

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
s.bind((host, port))
s.listen(backlog)
print 'Serving HTTP on port %s ...' % port
while True:
    client, address = s.accept()
    data = client.recv(size)
    request_method = data.split(' ')[0]
    print(request_method)
    print(data)
    if request_method == 'GET':
	    http_response = """\
			HTTP/1.1 200 OK

			Hello, World!
			"""
	    client.sendall(http_response)
    client.close()
