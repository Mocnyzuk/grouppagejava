package com.grouppage.domain.response;

import com.grouppage.domain.notmapped.GroupLight;
import com.grouppage.domain.notmapped.PostLight;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {

    private GroupLight group;
    private List<PostLight> posts;


}
