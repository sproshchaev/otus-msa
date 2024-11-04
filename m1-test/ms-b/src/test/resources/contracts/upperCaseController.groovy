package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return uppercase"

    request {
        url "/transform"
        method GET()
    }

    response {
        status OK()
    }
}
