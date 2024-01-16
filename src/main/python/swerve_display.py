from client import PyFRCClient
import pygame

IP = "10.81.93.2"
PORT = 6969

WIDTH,HEIGHT = 800,600

SWERVE_KEY = "swerve"

if __name__ == '__main__':
    screen = pygame.display.set_mode((WIDTH, HEIGHT))
    pygame.init()
    pygame.display.init()

    client = PyFRCClient(IP, PORT)
    running = True
    while running:
        for evt in pygame.event.get():
            if evt.type == pygame.QUIT:
                running = False
        screen.fill((0,0,0))

            # Get the value from SmartDashboard (through my pyFRC protocol)
        flX = client.get(SWERVE_KEY + "_flX")
        flY = client.get(SWERVE_KEY + "_flY")
        
        frX = client.get(SWERVE_KEY + "_frX")
        frY = client.get(SWERVE_KEY + "_frY")
        
        blX = client.get(SWERVE_KEY + "_blX")
        blY = client.get(SWERVE_KEY + "_blY")
        
        brX = client.get(SWERVE_KEY + "_brX")
        brY = client.get(SWERVE_KEY + "_brY")

        pygame.draw.line(screen, (255, 255, 255), (WIDTH/4        ,HEIGHT/4         ), ((WIDTH / 4)             + flX*50, HEIGHT / 4              - flY*50), width = 5)
        pygame.draw.line(screen, (255, 255, 255), (WIDTH/4+WIDTH/2,HEIGHT/4         ), ((WIDTH / 4)+(WIDTH / 2) + frX*50, HEIGHT / 4              - frY*50), width = 5)
        pygame.draw.line(screen, (255, 255, 255), (WIDTH/4        ,HEIGHT/4+HEIGHT/2), ((WIDTH / 4)             + blX*50, HEIGHT / 4+(HEIGHT / 2) - blY*50), width = 5)
        pygame.draw.line(screen, (255, 255, 255), (WIDTH/4+WIDTH/2,HEIGHT/4+HEIGHT/2), ((WIDTH / 4)+(WIDTH / 2) + brX*50, HEIGHT / 4+(HEIGHT / 2) - brY*50), width = 5)
        pygame.display.flip()