def call(def image) {
    try {
        sh "docker images --filter 'reference=${image}*' --format \"{{.Tag}} {{.Repository}}:{{.Tag}}\" | sort -n | awk '{ print \$2 }' | xargs --no-run-if-empty docker rmi"

    } catch (ignored) {
        // this would make the entire popeline fail. We don't want that
        println ignored
    }
}
