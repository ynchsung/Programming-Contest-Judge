#! /usr/bin/env python3
import sys
import struct
import socket

class DataStream:
    def __init__(self, ip, port):
        self.sock = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
        self.sock.connect((ip, port))

    def read_utf(self):
        utf_length = struct.unpack('>H', self.sock.recv(2))[0]
        return self.sock.recv(utf_length)

    def write_utf(self, msg):
        msg = bytes(msg, 'UTF-8')
        size = len(msg)
        self.sock.send(struct.pack('!H', size))
        self.sock.send(msg)

sock = DataStream(sys.argv[1], int(sys.argv[2]))
while True:
    msg = input()
    sock.write_utf(msg)
    back = sock.read_utf()
    print(back)
