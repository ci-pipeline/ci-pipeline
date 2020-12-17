def call(ObjectModel model) {

    return Network.create(model.services.size() > 0)
}

class Network {
    String id
    boolean hasServices

    static Network create(boolean hasServices) {
        def networkId = UUID.randomUUID().toString()

        if (hasServices) {
            println("creating network: ${networkId}")
            runShell "docker network create --name ${networkId}"
        }

        return new Network(id: networkId, hasServices: hasServices)
    }

    def remove() {
        if (hasServices) {
            println("removing network: ${id}")
            runShell "docker network rm ${id}"
        }
    }

    def getCliArgs() {
        if (hasServices) {
            return "--net=${id}"
        }
        return ""
    }
}
