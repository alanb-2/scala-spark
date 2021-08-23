#!/bin/sh

usage()
{
  echo "Usage: $0 [ -c CLUSTER_NAME ] [ -n REGISTRY_NAME ] [ -p REGISTRY_PORT ]"
  exit 2
}

while getopts 'c:n:p:?h' option
do
  case ${option} in
    c) cluster_name=$OPTARG ;;
    n) registry_name=$OPTARG ;;
    p) registry_port=$OPTARG ;;
    h|?) usage ;;
  esac
done

set -o errexit

echo "Creating registry container \"${registry_name}\" connected to 127.0.0.1:${registry_port}"
running="$(docker inspect -f '{{.State.Running}}' "${registry_name}" 2>/dev/null || true)"
if [ "${running}" != true ]; then
  docker run \
    -d --restart=always -p "127.0.0.1:${registry_port}" --name "${registry_name}" \
    registry:2
fi

echo "Creating kind cluster with local registry container \"${registry_name}\" enabled in containerd"
cat <<EOF | kind create cluster --name=${cluster_name} --config=-
kind: Cluster
apiVersion: kind.x-k8s.io/v1alpha4
containerdConfigPatches:
- |-
  [plugins."io.containerd.grpc.v1.cri".registry.mirrors."localhost:${registry_port}"]
    endpoint = ["http://${registry_name}:${registry_port}"]
nodes:
- role: control-plane
  extraPortMappings:
  - containerPort: 30090
    hostPort: 9000
    protocol: TCP
EOF

echo "Connecting registry to the cluster network (if not already connected)"
docker network connect "kind" "${registry_name}" || true

echo "Documenting the local registry: https://github.com/kubernetes/enhancements/tree/master/keps/sig-cluster-lifecycle/generic/1755-communicating-a-local-registry"
cat <<EOF | kubectl apply -f -
apiVersion: v1
kind: ConfigMap
metadata:
  name: local-registry-hosting
  namespace: kube-public
data:
  localRegistryHosting.v1: |
    host: "localhost:${registry_port}"
    help: "https://kind.sigs.k8s.io/docs/user/local-registry/"
EOF
