def call() {
    sh """
        [ -f "/usr/local/bin/docker" ] || { 
           wget -O /tmp/docker.tgz https://download.docker.com/linux/static/stable/x86_64/docker-20.10.1.tgz
           tar xzf /tmp/docker.tgz -C /tmp
           cp -rf /tmp/docker/docker /usr/local/bin
        }
    """
}
