import socket
import struct

class PacketType(object):
    PING = 0
    GET = 1

class PyFRCClient(object):
    def __init__(self, ip, port):
        self.conn = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.conn.connect((ip, port))

    def send(self, data):
        self.conn.send(data)
    def recv(self, size=1024):
        return self.conn.recv(size)
    def send_ping(self):
            # ping packet
        self.send(bytes([PacketType.PING]) + b"\0\0")
        assert self.recv() == bytes([PacketType.PING]) + b"\0\0"
    def get(self, name: str) -> float:
            # get packet
        self.send(bytes([PacketType.GET, len(name) // 256, len(name) % 256]) + bytes(name, 'utf-8'))

        res = self.recv(3)
        if res[0] != PacketType.GET:
            raise ValueError(f"got unexpected packet id, expected {PacketType.GET}, got {res[0]}")
        data: bytes = self.recv(8)
        if len(data) != 8:
            raise ValueError(f"got unexpected packet size, expected 8, got {len(data)}")
        return struct.unpack('>d', data)[0]


if __name__ == '__main__':
    client = PyFRCClient("127.0.0.1", 6969)
    client.send_ping()
    print("Ping completed")
    pass