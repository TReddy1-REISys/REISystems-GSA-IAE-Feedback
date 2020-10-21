application_environments{
    dev {
        toggle_tests{
            skip_unit_tests = true
            run_sonarqube_tests = true
            run_api_tests = false
        }
	  newman_config{
            env_file = "env_vars.json"
            collections{
                test_collection_data = "test_collection_data.csv"
            }
        }
    }
    test {
        toggle_tests{
            skip_unit_tests = true
            run_sonarqube_tests = false
            run_api_tests = false
        }
  newman_config{
            env_file = "env_vars.json"
            collections{
                test_collection = "test_collection.csv"
            }
        }
    }
    staging {
        toggle_tests{
            skip_unit_tests = true
            run_sonarqube_tests = true
            run_api_tests = false
        }
  newman_config{
            env_file = "env_vars.json"
            collections{
                test_collection = "test_collection.csv"
            }
        }
    }
    prod {
        toggle_tests{
            skip_unit_tests = true
            run_sonarqube_tests = true
            run_api_tests = false
        }
  newman_config{
            env_file = "env_vars.json"
            collections{
                test_collection = "test_collection.csv"
            }
        }
    }
    login {
        toggle_tests{
            skip_unit_tests = true
            run_sonarqube_tests = true
            run_api_tests = false
        }
  newman_config{
            env_file = "env_vars.json"
            collections{
                test_collection = "test_collection.csv"
            }
        }
    }
}

steps{
    // Use the following unit_test if deploying a spring application
    unit_test{
        stage = "Unit Test"
        image = "maven"
        command = "mvn test -Dmaven.test.failure.ignore=false"
    }
}