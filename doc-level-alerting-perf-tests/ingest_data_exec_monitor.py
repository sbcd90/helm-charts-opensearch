from locust import HttpUser, task, between
import uuid


class IngestDataAndExecMonitor(HttpUser):
    wait_time = between(10, 15)
    @task
    def ingest_data_exec_monitor(self):
        data = self.prep_data(count=100)
#        print(data)
        self.client.post("/http-logs/_bulk?refresh", allow_redirects=True, data=str.encode(data), headers={"Content-Type": "application/json"})

        resp = self.client.post("/_plugins/_alerting/monitors/0PzJzoABHUOdA0q5OEWJ/_execute", headers={"Content-Type": "application/json"})
        print(resp.json())

    def prep_data(self, count=50):
        first_line = "{ \"index\" : { \"_id\": \"$id\" } }\n"
        second_line = '{"@timestamp": 893966434, "clientip":"128.1.0.0", "request": "GET /english/images/nav_home_off.gif HTTP/1.0", "status": 200, "size": 11978}\n'
        json = ""
        for i in range(count):
            json += first_line.replace('$id', str(uuid.uuid4())) + second_line
        return json
