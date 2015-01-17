/*
 * Copyright [2015] Mario Mohr <mario_mohr@web.de>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.mamohr.gradle.deploymentstructure.model

import org.gradle.util.ConfigureUtil

/**
 * This class represents a deployment in the jboss-deployment-structure.xml
 */
class Deployment {
    private Set<DependencyModule> dependencyModules = []
    private Set<Module> excludeModules = [] as Set

    /**
     * Adds a dependency module to the deployment.
     *
     * @param moduleIdentifier A module identifier in the form 'module.name:slot'. If no slot is given 'main' is assumed.
     */
    void dependency(String moduleIdentifier) {
        DependencyModule dependencyModule = new DependencyModule(moduleIdentifier)
        dependencyModules.add(dependencyModule)
    }

    void dependency(String moduleIdentifier, Closure<DependencyModule> closure) {
        DependencyModule dependencyModule = new DependencyModule(moduleIdentifier)
        dependencyModules.add(dependencyModule)
        ConfigureUtil.configure(closure, dependencyModule)
    }

    void exclude(String moduleIdentifier) {
        Module excludedModule = new Module(moduleIdentifier)
        excludeModules.add(excludedModule)
    }

    void exclude(Module module) {
        excludeModules.add(module)
    }

    def saveToXml(Node node) {
        Node deployment = new Node(node, getXmlNodeName())
        Node dependencies = new Node(deployment, "dependencies")
        dependencyModules.each { module ->
            module.saveToXml(dependencies);
        }
        Node exclusions = new Node(deployment, "exclusions")
        excludeModules.each { ex -> ex.saveToXml(exclusions) }
        return deployment
    }

    protected String getXmlNodeName() {
        return 'deployment'
    }
}
