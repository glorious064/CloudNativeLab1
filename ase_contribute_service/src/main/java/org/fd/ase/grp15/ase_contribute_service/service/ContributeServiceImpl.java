package org.fd.ase.grp15.ase_contribute_service.service;

import org.apache.dubbo.config.annotation.DubboReference;
import org.fd.ase.grp15.ase_contribute_service.entity.ContributeStatus;
import org.fd.ase.grp15.ase_contribute_service.entity.Contribution;
import org.fd.ase.grp15.ase_contribute_service.entity.vo.ListContribution;
import org.fd.ase.grp15.ase_contribute_service.repository.ContributeRepository;
import org.fd.ase.grp15.ase_contribute_service.request.ContributeRequest;
import org.fd.ase.grp15.common.enums.ConferenceRole;
import org.fd.ase.grp15.common.iservice.IConferenceService;
import org.fd.ase.grp15.common.iservice.IUserConferenceRoleService;
import org.fd.ase.grp15.common.iservice.conference.dto.ConferenceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

@Service
public class ContributeServiceImpl {

    @Autowired
    private ContributeRepository contributeRepository;
    @DubboReference(check = false)
    private IUserConferenceRoleService iUserConferenceRoleService;

    @DubboReference(check = false)
    private IConferenceService conferenceService;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String contribute(ContributeRequest.In in) {
        //Todo
        // 步骤如下：
        // 1. 调用conferenceService.getConferenceInfoByName获取会议信息(基于dubbo的rpc调用)
        // 2. 检查会议状态是否为“投稿中”，以及投稿截止时间是否已过
        // 3. 调用iUserConferenceRoleService.addRoleToUserInConference给用户添加author身份(基于dubbo的rpc调用)
        // 4. 创建Contribution对象并保存到数据库
        // 如果在过程中出现异常，可以抛出RuntimeException
        ConferenceDTO conferenceInfo = conferenceService.getConferenceInfoByName(in.getConferenceName());
        if (conferenceInfo == null) {
            throw new RuntimeException("Conference not found");
        }
        if (!conferenceInfo.getConferenceStatus().equals("投稿中")) {
            throw new RuntimeException("Conference not in submission phase");
        }
        if (conferenceInfo.getSubmissionDeadline().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Submission deadline has passed");
        }

        iUserConferenceRoleService.addRoleToUserInConference(in.getUsername(), in.getConferenceName(), ConferenceRole.AUTHOR);
        Contribution contribution = new Contribution(in.getUsername(),
                in.getRealName(), in.getConferenceName(), in.getTitle(),
                in.getAbstractContent(), in.getEssayId(),  sdf.format(new Date()));
        contributeRepository.save(contribution);

        return null;
    }


    public List<ListContribution> listContributionsByUsername(String author) {
        //Todo
        return contributeRepository.findAllByAuthor(author).stream()
                .map(contribution -> new ListContribution(contribution.getId(),
                        contribution.getConferenceName(), contribution.getContributeTime(),
                        contribution.getTitle(), contribution.getStatus()))
                .toList();
    }

    public List<ListContribution> listContibutionsByConferenceName(String name) {
        // TODO
        return contributeRepository.findAllByConferenceName(name).stream()
                .map(contribution -> new ListContribution(contribution.getId(),
                        contribution.getConferenceName(), contribution.getContributeTime(),
                        contribution.getTitle(), contribution.getStatus()))
                .toList();
    }

    public Contribution detailById(String idStr) {
        // TODO
        long id = Long.parseLong(idStr);
        return contributeRepository.findContributionById(id);
    }
}
