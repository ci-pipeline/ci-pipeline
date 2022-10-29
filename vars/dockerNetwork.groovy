def call(ObjectModel model) {

    def hasOnlyDockerService = model.services.size() == 1 && ObjectModel.isDockerService(model.services[0])
    def hasServices = !hasOnlyDockerService && model.services.size() > 0

    return Network.create(hasServices, this)
}

class Network {
    String id
    boolean hasServices
    def node

    static Network create(boolean hasServices, def node) {

        def networkId = UUID.randomUUID().toString()

        if (hasServices) {
            println("creating network: ${networkId}")
            node.sh "docker network create ${networkId}"
        }

        return new Network(id: networkId, hasServices: hasServices, node: node)
    }

    def remove() {
        if (hasServices) {
            println("removing network: ${id}")
            node.sh "docker network rm ${id}"
        }
    }

    def getCliArgs() {
        if (hasServices) {
            return " --net=${id} "
        }
        return ""
    }
}
