output "cluster_name" {
  description = "GKE Cluster Name"
  value       = google_container_cluster.primary.name
}

output "cluster_endpoint" {
  description = "GKE Cluster Endpoint"
  value       = google_container_cluster.primary.endpoint
  sensitive   = true
}

output "cluster_ca_certificate" {
  description = "GKE Cluster CA Certificate"
  value       = google_container_cluster.primary.master_auth[0].cluster_ca_certificate
  sensitive   = true
}

output "cluster_location" {
  description = "GKE Cluster Location"
  value       = google_container_cluster.primary.location
}

output "node_pool_name" {
  description = "Node Pool Name"
  value       = google_container_node_pool.primary_nodes.name
}

output "service_account_email" {
  description = "Service Account Email for GKE nodes"
  value       = google_service_account.gke_sa.email
}

output "kubectl_connection_command" {
  description = "Command to connect kubectl to this cluster"
  value       = "gcloud container clusters get-credentials ${google_container_cluster.primary.name} --zone=${var.zone} --project=${var.project_id}"
}
