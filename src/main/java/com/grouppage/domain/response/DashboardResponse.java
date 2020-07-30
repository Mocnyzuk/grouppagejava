package com.grouppage.domain.response;

import com.grouppage.domain.entity.Group;
import com.grouppage.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {

    private Group group;
    private List<Post> posts;


}
