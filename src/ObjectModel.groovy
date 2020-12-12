class ObjectModel {

    public String image
    public List<Step> steps = new ArrayList<>()

    static class Step {
        public String name
        public String when
        public List<String> actions
        public List<Step> parallel

        boolean isParallelStep() {
            return parallel != null && parallel.size() > 0
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
        List<String> actions = new ArrayList<>();
        yaml.actions.each {
            actions.add(it)
        }
        return new Step(
                name: yaml.name,
                when: yaml.when,
                actions: actions
        )
    }
}
