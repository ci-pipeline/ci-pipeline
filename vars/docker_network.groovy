def call(ObjectModel model) {

    return Network.create(model.services.size() > 0, this)
}

class Network {
    String id
    boolean hasServices
    def node

    static Network create(boolean hasServices, def node) {

        println(node)
        println(node.getClass())

        def networkId = UUID.randomUUID().toString()

        if (hasServices) {
            println("creating network: ${networkId}")
            node.sh "docker network create --name ${networkId}"
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
            return "--net=${id}"
        }
        return ""
    }
}
