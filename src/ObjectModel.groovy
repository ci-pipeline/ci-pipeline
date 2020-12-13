class ObjectModel {

    // the container image the used to run the build
    public String image
    public List<Step> steps = new ArrayList<>()

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

        boolean isParallelStep() {
            return parallel.size() > 0
        }
    }

    static ObjectModel load(def yaml) {
        ObjectModel model = new ObjectModel()
        model.image = yaml.image
        loadSteps(yaml, model)

        return model
    }

    private static Object loadSteps(yaml, model) {
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

    private static Step newStep(def yaml) {
        List<String> actions = new ArrayList<>()
        yaml.actions.each { actions.add(it) }

        List<String> only = new ArrayList<>()
        yaml.only.each { only.add(it) }

        List<String> except = new ArrayList<>()
        yaml.only.each { except.add(it) }

        return new Step(
                name: yaml.name,
                only: only,
                except: except,
                actions: actions
        )
    }
}
