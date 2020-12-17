def call(ObjectModel model) {

    return Network.create(model.services.size() > 0, sh)
}

class Network {
    String id
    boolean hasServices
    def sh

    static Network create(boolean hasServices, sh) {
        def networkId = UUID.randomUUID().toString()

        if (hasServices) {
            println("creating network: ${networkId}")
            sh "docker network create --name ${networkId}"
        }

        return new Network(id: networkId, hasServices: hasServices, sh: sh)
    }

    def remove() {
        if (hasServices) {
            println("removing network: ${id}")
            sh "docker network rm ${id}"
        }
    }

    def getCliArgs() {
        if (hasServices) {
            return "--net=${id}"
        }
        return ""
    }
}
