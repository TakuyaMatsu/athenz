/*
 * Copyright The Athenz Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yahoo.athenz.zms;

import com.yahoo.athenz.common.server.store.PrincipalGroup;
import com.yahoo.rdl.Timestamp;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;

public class PrincipalGroupTest {

    private final ZMSTestInitializer zmsTestInitializer = new ZMSTestInitializer();

    @BeforeClass
    public void startMemoryMySQL() {
        zmsTestInitializer.startMemoryMySQL();
    }

    @AfterClass
    public void stopMemoryMySQL() {
        zmsTestInitializer.stopMemoryMySQL();
    }

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        zmsTestInitializer.setUp();
    }

    @Test
    public void testPrincipalGroup() {
        PrincipalGroup prGroup = new PrincipalGroup();
        prGroup.setGroupName("role");
        prGroup.setDomainName("domain");
        prGroup.setDomainUserAuthorityFilter("authority");

        assertEquals(prGroup.getGroupName(), "role");
        assertEquals(prGroup.getDomainName(), "domain");
        assertEquals(prGroup.getDomainUserAuthorityFilter(), "authority");
    }

    @Test
    public void testListDomainGroupMembers() {

        String domainName = "listdomaingroupmembers";

        ZMSImpl zmsImpl = zmsTestInitializer.getZms();
        RsrcCtxWrapper ctx = zmsTestInitializer.getMockDomRsrcCtx();
        final String auditRef = zmsTestInitializer.getAuditRef();

        TopLevelDomain dom1 = zmsTestInitializer.createTopLevelDomainObject(domainName,
                "Test Domain1", "testOrg", zmsTestInitializer.getAdminUser());
        zmsImpl.postTopLevelDomain(ctx, auditRef, null, dom1);

        Group group1 = zmsTestInitializer.createGroupObject(domainName, "group1", "user.jack", "user.janie");
        zmsImpl.putGroup(ctx, domainName, "group1", auditRef, false, null, group1);

        Group group2 = zmsTestInitializer.createGroupObject(domainName, "group2", "user.janie", "user.jane");
        zmsImpl.putGroup(ctx, domainName, "group2", auditRef, false, null, group2);

        Group group3 = zmsTestInitializer.createGroupObject(domainName, "group3", "user.jack", "user.jane");
        zmsImpl.putGroup(ctx, domainName, "group3", auditRef, false, null, group3);

        Group group4 = zmsTestInitializer.createGroupObject(domainName, "group4", "user.jack", null);
        zmsImpl.putGroup(ctx, domainName, "group4", auditRef, false, null, group4);

        Group group5 = zmsTestInitializer.createGroupObject(domainName, "group5", "user.jack-service", "user.jane");
        zmsImpl.putGroup(ctx, domainName, "group5", auditRef, false, null, group5);

        DomainGroupMembers domainGroupMembers = zmsImpl.getDomainGroupMembers(ctx, domainName);
        assertEquals(domainGroupMembers.getDomainName(), domainName);

        List<DomainGroupMember> members = domainGroupMembers.getMembers();
        assertNotNull(members);
        assertEquals(members.size(), 4);
        assertTrue(ZMSTestUtils.verifyDomainGroupMember(members, "user.jack", "group1", "group3", "group4"));
        assertTrue(ZMSTestUtils.verifyDomainGroupMember(members, "user.janie", "group1", "group2"));
        assertTrue(ZMSTestUtils.verifyDomainGroupMember(members, "user.jane", "group2", "group3", "group5"));
        assertTrue(ZMSTestUtils.verifyDomainGroupMember(members, "user.jack-service", "group5"));

        zmsImpl.deleteTopLevelDomain(ctx, domainName, auditRef, null);
    }

    @Test
    public void testGroupMemberUpdateWithEmptyPendingState() {

        final String domainName = "group-member-update";
        final String groupName = "group1";

        ZMSImpl zmsImpl = zmsTestInitializer.getZms();
        RsrcCtxWrapper ctx = zmsTestInitializer.getMockDomRsrcCtx();
        final String auditRef = zmsTestInitializer.getAuditRef();

        TopLevelDomain dom1 = zmsTestInitializer.createTopLevelDomainObject(domainName,
                "Test Domain1", "testOrg", zmsTestInitializer.getAdminUser());
        zmsImpl.postTopLevelDomain(ctx, auditRef, null, dom1);

        // let's add the group with 2 members

        Group group1 = zmsTestInitializer.createGroupObject(domainName, groupName, "user.jack", "user.janie");
        zmsImpl.putGroup(ctx, domainName, groupName, auditRef, false, null, group1);

        Group groupRes = zmsImpl.getGroup(ctx, domainName, groupName, false, false);
        assertEquals(groupRes.getGroupMembers().size(), 2);
        zmsTestInitializer.checkGroupMember(Arrays.asList("user.jack", "user.janie"), groupRes.getGroupMembers());

        // now let's modify the same group without any changes

        zmsImpl.putGroup(ctx, domainName, groupName, auditRef, false, null, group1);

        groupRes = zmsImpl.getGroup(ctx, domainName, groupName, false, false);
        assertEquals(groupRes.getGroupMembers().size(), 2);
        zmsTestInitializer.checkGroupMember(Arrays.asList("user.jack", "user.janie"), groupRes.getGroupMembers());

        // now let's set an expiry for jack, and we're also going to set the pending
        // state to empty string which is done by json deserializer

        Timestamp now = Timestamp.fromCurrentTime();
        for (GroupMember member : group1.getGroupMembers()) {
            if (member.getMemberName().equals("user.jack")) {
                member.setExpiration(now);
                member.setPendingState("");
            }
        }

        zmsImpl.putGroup(ctx, domainName, groupName, auditRef, false, null, group1);

        groupRes = zmsImpl.getGroup(ctx, domainName, groupName, false, false);
        assertEquals(groupRes.getGroupMembers().size(), 2);
        zmsTestInitializer.checkGroupMember(Arrays.asList("user.jack", "user.janie"), groupRes.getGroupMembers());

        for (GroupMember member : groupRes.getGroupMembers()) {
            if (member.getMemberName().equals("user.jack")) {
                assertEquals(member.getExpiration(), now);
            }
        }

        zmsImpl.deleteTopLevelDomain(ctx, domainName, auditRef, null);
    }
}
