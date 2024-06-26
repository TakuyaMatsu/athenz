//
// This file generated by rdl 1.5.2. Do not modify!
//

package com.yahoo.athenz.msd;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import com.yahoo.rdl.*;

//
// KubernetesNetworkPolicyIngressRule - Kubernetes network policy ingress rule
//
@JsonIgnoreProperties(ignoreUnknown = true)
public class KubernetesNetworkPolicyIngressRule {
    @RdlOptional
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public List<KubernetesNetworkPolicyPeer> from;
    @RdlOptional
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public List<KubernetesNetworkPolicyPort> ports;

    public KubernetesNetworkPolicyIngressRule setFrom(List<KubernetesNetworkPolicyPeer> from) {
        this.from = from;
        return this;
    }
    public List<KubernetesNetworkPolicyPeer> getFrom() {
        return from;
    }
    public KubernetesNetworkPolicyIngressRule setPorts(List<KubernetesNetworkPolicyPort> ports) {
        this.ports = ports;
        return this;
    }
    public List<KubernetesNetworkPolicyPort> getPorts() {
        return ports;
    }

    @Override
    public boolean equals(Object another) {
        if (this != another) {
            if (another == null || another.getClass() != KubernetesNetworkPolicyIngressRule.class) {
                return false;
            }
            KubernetesNetworkPolicyIngressRule a = (KubernetesNetworkPolicyIngressRule) another;
            if (from == null ? a.from != null : !from.equals(a.from)) {
                return false;
            }
            if (ports == null ? a.ports != null : !ports.equals(a.ports)) {
                return false;
            }
        }
        return true;
    }
}
