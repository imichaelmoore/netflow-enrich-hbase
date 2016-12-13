clean:
	docker-compose kill
	docker-compose rm -f

start:
	docker-compose up

demo:

	@echo "Creating HBase table..."
	docker-compose run hbase /bin/bash -c "echo \"create 'netflow', 'metadata'\" | /opt/hbase-1.2.4/bin/hbase shell"
	@echo ""
	@echo ""
	@echo ""
	@echo ""

	@echo "Submitting Storm topology..."
	docker-compose run storm_workspace /bin/bash -c "/opt/apache-storm-1.0.2/bin/storm jar /opt/topology/target/sandbox-1.0-SNAPSHOT-jar-with-dependencies.jar Topology"
	@echo ""
	@echo ""
	@echo ""
	@echo ""

	@echo "Waiting for Storm topology to settle to settle..."
	sleep 20
	@echo ""
	@echo ""
	@echo ""
	@echo ""


	@echo "Sending Netflow to collector and Kafka...."
	docker-compose run generator /bin/bash -c "softflowd -v1 -r /opt/inside.tcpdump -n collector:50000"
	@echo ""
	@echo ""
	@echo ""
	@echo ""

	@echo "Waiting for stream to get processed..."
	sleep 20
	@echo ""
	@echo ""
	@echo ""
	@echo ""

	@echo "Querying Hbase...."
	docker-compose run hbase /bin/bash -c "echo \"scan 'netflow'\" | /opt/hbase-1.2.4/bin/hbase shell"
	@echo ""
	@echo ""
	@echo ""
	@echo ""


