import time
from locust import HttpUser, task, between


class CreateMonitorUser(HttpUser):
    wait_time = between(1, 5)
    @task
    def create_monitor(self):
        self.client.post("/_plugins/_alerting/monitors",
                         allow_redirects=True,
                         json={
                             "type": "monitor",
                             "monitor_type": "doc_level_monitor",
                             "name": "iad-monitor1",
                             "enabled": True,
                             "createdBy": "chip",
                             "schedule": {
                                 "period": {
                                     "interval": 1,
                                     "unit": "MINUTES"
                                 }
                             },
                             "inputs": [
                                 {
                                     "doc_level_input": {
                                         "description": "windows-powershell",
                                         "indices": [
                                             "test-logs"
                                         ],
                                         "queries": [
                                             {
                                                 "id": "sigma-123",
                                                 "name": "sigma-123",
                                                 "query": "region:us_west_2",
                                                 "severity": "5",
                                                 "tags": [
                                                     "MITRE:8500"
                                                 ]
                                             },
                                             {
                                                 "id": "sigma-456",
                                                 "name": "sigma-456",
                                                 "query": "region:us_east_1",
                                                 "severity": "5",
                                                 "tags": [
                                                     "MITRE:8600"
                                                 ]
                                             },
                                             {
                                                 "id": "sigma-789",
                                                 "name": "sigma-789",
                                                 "query": "region:us_east_2",
                                                 "severity": "5",
                                                 "tags": [
                                                     "MITRE:8600"
                                                 ]
                                             },
                                             {
                                                 "id": "sigma-101112",
                                                 "name": "sigma-101112",
                                                 "query": "region:us_west_1",
                                                 "severity": "5",
                                                 "tags": [
                                                     "MITRE:8600"
                                                 ]
                                             },
                                             {
                                                 "id": "sigma-141516",
                                                 "name": "sigma-141516",
                                                 "query": "region:af_south_1",
                                                 "severity": "5",
                                                 "tags": [
                                                     "MITRE:8600"
                                                 ]
                                             },
                                             {
                                                 "id": "sigma-171819",
                                                 "name": "sigma-171819",
                                                 "query": "region:ap_east_1",
                                                 "severity": "5",
                                                 "tags": [
                                                     "MITRE:8600"
                                                 ]
                                             },
                                             {
                                                 "id": "sigma-202122",
                                                 "name": "sigma-202122",
                                                 "query": "region:ap_south_1",
                                                 "severity": "5",
                                                 "tags": [
                                                     "MITRE:8600"
                                                 ]
                                             },
                                             {
                                                 "id": "sigma-232425",
                                                 "name": "sigma-232425",
                                                 "query": "region:ap_southeast_3",
                                                 "severity": "5",
                                                 "tags": [
                                                     "MITRE:8600"
                                                 ]
                                             },
                                             {
                                                 "id": "sigma-262728",
                                                 "name": "sigma-262728",
                                                 "query": "region:ap_northeast_3",
                                                 "severity": "5",
                                                 "tags": [
                                                     "MITRE:8600"
                                                 ]
                                             },
                                             {
                                                 "id": "sigma-293031",
                                                 "name": "sigma-293031",
                                                 "query": "region:ap_northeast_2",
                                                 "severity": "5",
                                                 "tags": [
                                                     "MITRE:8600"
                                                 ]
                                             },
                                             {
                                                 "id": "sigma-323334",
                                                 "name": "sigma-323334",
                                                 "query": "region:ap_southeast_1",
                                                 "severity": "5",
                                                 "tags": [
                                                     "MITRE:8600"
                                                 ]
                                             },
                                             {
                                                 "id": "sigma-353637",
                                                 "name": "sigma-353637",
                                                 "query": "region:ap_southeast_2",
                                                 "severity": "5",
                                                 "tags": [
                                                     "MITRE:8600"
                                                 ]
                                             },
                                             {
                                                 "id": "sigma-383940",
                                                 "name": "sigma-383940",
                                                 "query": "region:ap_northeast_1",
                                                 "severity": "5",
                                                 "tags": [
                                                     "MITRE:8600"
                                                 ]
                                             },
                                             {
                                                 "id": "sigma-414243",
                                                 "name": "sigma-414243",
                                                 "query": "region:ca_central_1",
                                                 "severity": "5",
                                                 "tags": [
                                                     "MITRE:8600"
                                                 ]
                                             },
                                             {
                                                 "id": "sigma-444546",
                                                 "name": "sigma-444546",
                                                 "query": "region:eu_central_1",
                                                 "severity": "5",
                                                 "tags": [
                                                     "MITRE:8600"
                                                 ]
                                             },
                                             {
                                                 "id": "sigma-474849",
                                                 "name": "sigma-474849",
                                                 "query": "region:eu_west_1",
                                                 "severity": "5",
                                                 "tags": [
                                                     "MITRE:8600"
                                                 ]
                                             },
                                             {
                                                 "id": "sigma-505152",
                                                 "name": "sigma-505152",
                                                 "query": "region:eu_west_2",
                                                 "severity": "5",
                                                 "tags": [
                                                     "MITRE:8600"
                                                 ]
                                             },
                                             {
                                                 "id": "sigma-535455",
                                                 "name": "sigma-535455",
                                                 "query": "region:eu_south_1",
                                                 "severity": "5",
                                                 "tags": [
                                                     "MITRE:8600"
                                                 ]
                                             },
                                             {
                                                 "id": "sigma-565758",
                                                 "name": "sigma-565758",
                                                 "query": "region:eu_west_3",
                                                 "severity": "5",
                                                 "tags": [
                                                     "MITRE:8600"
                                                 ]
                                             },
                                             {
                                                 "id": "sigma-596061",
                                                 "name": "sigma-596061",
                                                 "query": "region:eu_north_1",
                                                 "severity": "5",
                                                 "tags": [
                                                     "MITRE:8600"
                                                 ]
                                             }
                                         ]
                                     }
                                 }
                             ],
                             "triggers": [ { "document_level_trigger": {
                                 "name": "test-trigger",
                                 "severity": "1",
                                 "condition": {
                                     "script": {
                                         "source": "params['sigma-123'] && (!params['sigma-456'] || false)",
                                         "lang": "painless"
                                     }
                                 }
                             }}]
                         })
