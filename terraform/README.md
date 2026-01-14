# Terraform Infrastructure for GitOps Platform

This Terraform configuration creates the GKE infrastructure for the maven-hello-world GitOps platform.

## What This Creates

- **GKE Cluster**: Kubernetes cluster with 2 e2-medium nodes
- **Node Pool**: Separately managed node pool with autoscaling (2-10 nodes)
- **Service Account**: Dedicated service account for GKE nodes with minimal permissions
- **IAM Bindings**: Logging, monitoring, and metrics permissions

## Prerequisites

1. **Install Terraform**
```bash
   # Ubuntu/Debian
   wget -O- https://apt.releases.hashicorp.com/gpg | sudo gpg --dearmor -o /usr/share/keyrings/hashicorp-archive-keyring.gpg
   echo "deb [signed-by=/usr/share/keyrings/hashicorp-archive-keyring.gpg] https://apt.releases.hashicorp.com $(lsb_release -cs) main" | sudo tee /etc/apt/sources.list.d/hashicorp.list
   sudo apt update && sudo apt install terraform
```

2. **Authenticate with GCP**
```bash
   gcloud auth application-default login
```

3. **Enable Required APIs**
```bash
   gcloud services enable container.googleapis.com
   gcloud services enable compute.googleapis.com
```

## Usage

### Initial Setup

1. **Create terraform.tfvars from example**
```bash
   cp terraform.tfvars.example terraform.tfvars
   # Edit terraform.tfvars with your project ID
   nano terraform.tfvars
```

2. **Initialize Terraform**
```bash
   terraform init
```

3. **Review the plan**
```bash
   terraform plan
```

4. **Apply the configuration**
```bash
   terraform apply
```

### Connect to Cluster

After creation, connect kubectl:
```bash
# Get the connection command from output
terraform output kubectl_connection_command

# Or manually:
gcloud container clusters get-credentials maven-hello-world-cluster \
  --zone=us-central1-a \
  --project=YOUR_PROJECT_ID
```

### Update Configuration

1. Modify variables in `terraform.tfvars`
2. Review changes: `terraform plan`
3. Apply changes: `terraform apply`

### Disaster Recovery

To recreate the entire cluster:
```bash
# Destroy existing cluster (if needed)
terraform destroy

# Recreate
terraform apply
```

Then reinstall cluster components:
```bash
# cert-manager
kubectl apply -f https://github.com/cert-manager/cert-manager/releases/download/v1.13.2/cert-manager.yaml

# NGINX Ingress
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.9.4/deploy/static/provider/cloud/deploy.yaml

# ArgoCD
kubectl create namespace argocd
kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml

# ArgoCD Applications
kubectl apply -f ../argocd/application.yaml
kubectl apply -f ../argocd/infrastructure-application.yaml
```

## Configuration Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `project_id` | GCP Project ID | (required) |
| `region` | GCP Region | `us-central1` |
| `zone` | GCP Zone | `us-central1-a` |
| `cluster_name` | Cluster name | `maven-hello-world-cluster` |
| `machine_type` | Node machine type | `e2-medium` |
| `node_count` | Initial nodes | `2` |
| `min_node_count` | Min nodes (autoscaling) | `2` |
| `max_node_count` | Max nodes (autoscaling) | `10` |
| `use_preemptible_nodes` | Use preemptible nodes | `false` |
| `environment` | Environment label | `demo` |

## Outputs

| Output | Description |
|--------|-------------|
| `cluster_name` | GKE cluster name |
| `cluster_endpoint` | Cluster API endpoint |
| `cluster_location` | Cluster location (zone) |
| `node_pool_name` | Node pool name |
| `service_account_email` | Node service account |
| `kubectl_connection_command` | kubectl connection command |

## Cost Estimate

With default configuration (2 × e2-medium nodes):
- **Cluster management**: Free
- **Nodes**: ~$28/month per node = $56/month total
- **Load Balancers**: ~$18/month
- **Total**: ~$74/month (₪244/month)

Using free trial credits: **$0/month** ✅

## Cleanup

To destroy all resources:
```bash
terraform destroy
```

⚠️ **Warning**: This will delete the cluster and all data. Make sure to backup anything important.

## Not Included (Installed Separately)

The following are installed manually after cluster creation:
- cert-manager (SSL certificates)
- NGINX Ingress Controller
- ArgoCD (GitOps tool)
- DNS configuration

## Best Practices Implemented

✅ Separate node pool management  
✅ Custom service account with minimal permissions  
✅ Workload Identity enabled  
✅ Auto-repair and auto-upgrade enabled  
✅ Autoscaling configured  
✅ Labels and tags for resource organization  
✅ Variables for easy customization  
✅ Outputs for integration  

## Troubleshooting

### Issue: Permission Denied

**Solution**: Ensure you're authenticated and have the necessary IAM roles:
```bash
gcloud auth application-default login
gcloud projects add-iam-policy-binding PROJECT_ID \
  --member="user:YOUR_EMAIL" \
  --role="roles/container.admin"
```

### Issue: API Not Enabled

**Solution**: Enable required APIs:
```bash
gcloud services enable container.googleapis.com
gcloud services enable compute.googleapis.com
```

### Issue: Quota Exceeded

**Solution**: Check quotas in GCP Console → IAM & Admin → Quotas

## Additional Resources

- [Terraform GCP Provider](https://registry.terraform.io/providers/hashicorp/google/latest/docs)
- [GKE Terraform Module](https://registry.terraform.io/modules/terraform-google-modules/kubernetes-engine/google/latest)
- [Terraform Best Practices](https://www.terraform.io/docs/cloud/guides/recommended-practices/index.html)
