OUTFILE = $(shell date +"%F_%H%M%S").sql.gz
.PHONY: import-db
import-db: ## Import db from kubernetes and restore it
	kubectl apply -f scripts/k8s-dumpdb.yaml
	kubectl wait -f scripts/k8s-dumpdb.yaml --for=condition=Ready
	kubectl exec psql-client -n bookstore -- pg_dump -O -x | gzip > $(OUTFILE)
	kubectl delete -f scripts/k8s-dumpdb.yaml
	docker-compose exec -T postgres psql -U bookstore -d postgres -c "drop database bookstore with (force);"
	docker-compose exec -T postgres createdb -U bookstore bookstore
	gunzip -c $(OUTFILE) | docker-compose exec -T postgres psql -U bookstore -d bookstore
	docker-compose restart flyway
