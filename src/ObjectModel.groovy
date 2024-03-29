class ObjectModel {

    private static final String DOCKER_SERVICE = "docker"

    // the container image the used to run the build
    public String image
    // in minutes
    public Integer timeout = 10

    public List<Step> steps = new ArrayList<>()
    // variables set as env vars
    public List<String> variables = new ArrayList<>()
    // image names to run as sidecar services
    public Set<String> services = new HashSet<>()

    static class Step {
        // step name
        public String name
        // branches or tags what only to run the step in
        public List<String> only = new ArrayList<>()
        // branches or tags that don't run the step in
        public List<String> except = new ArrayList<>()
        // actions to execute as part of the step
        public List<String> actions
        // parallel steps
        public List<Step> parallel = new ArrayList<>()
        // trigger: manual|automatic, default=automatic
        public String trigger

        boolean isParallelStep() {
            return parallel.size() > 0
        }

        boolean waitOnInput() {
            return "manual".equalsIgnoreCase(trigger)
        }
    }

    boolean hasDockerService() {
        return services.contains(DOCKER_SERVICE)
    }

    static boolean isDockerService(String serviceName) {
        return serviceName.equalsIgnoreCase(DOCKER_SERVICE)
    }

    static ObjectModel load(def yaml) {
        ObjectModel model = new ObjectModel()
        model.image = yaml.image

        loadSteps(yaml, model)
        loadVariables(yaml.variables, model)
        loadServices(yaml.services, model)

        return model
    }

    private static Object loadSteps(def yaml, ObjectModel model) {
        yaml.steps.each { YamlStep ->

            if (YamlStep.parallel != null) {

                List<Step> parallelSteps = new ArrayList<>()
                YamlStep.parallel.each { yamlParallelStep ->
                    parallelSteps.add(newStep(yamlParallelStep))
                }

                model.steps.add(new Step(
                        parallel: parallelSteps
                ))

            } else {
                model.steps.add(newStep(YamlStep))
            }
        }
    }

    private static def loadVariables(def yaml, ObjectModel model) {
        yaml.each { k, v ->
            model.variables.add("$k=$v")
        }
    }

    private static def loadServices(def yaml, ObjectModel model) {
        yaml.each { model.services.add(it) }
    }

    private static Step newStep(def yaml) {
        List<String> actions = new ArrayList<>()
        yaml.actions.each { actions.add(it) }

        List<String> only = new ArrayList<>()
        yaml.only.each { only.add(it) }

        List<String> except = new ArrayList<>()
        yaml.except.each { except.add(it) }

        return new Step(
                name: yaml.name,
                only: only,
                except: except,
                actions: actions,
                trigger: yaml.trigger
        )
    }
}
