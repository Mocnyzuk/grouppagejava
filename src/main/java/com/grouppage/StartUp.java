package com.grouppage;

import com.grouppage.domain.entity.*;
import com.grouppage.domain.entity.chat.Conversation;
import com.grouppage.domain.entity.chat.PrivateMessage;
import com.grouppage.domain.notmapped.HashTag;
import com.grouppage.domain.repository.*;
import com.grouppage.domain.repository.chat.ConversationRepository;
import com.grouppage.domain.repository.chat.PrivateMessageRepository;
import com.grouppage.domain.response.RegisterRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

//@Component
public class StartUp implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final GroupRepository groupRepository;
    private final ParticipantRepository participantRepository;
    private final ReactionRepository reactionRepository;
    private final PrivateMessageRepository privateMessageRepository;
    private final ConversationRepository conversationRepository;
    private final SignUpFormRepository signUpFormRepository;
    private final PasswordEncoder passwordEncoder;

    public Random rand = new Random();

    public List<User> users;
    public Reaction likes;
    public List<Group> groups;
    public List<Participant> participants = new ArrayList<>();

    public List<String> categories = Arrays.asList("IT", "KUCHNIA", "CARS", "OSWIETLENIE", "ELEKTRYKA", "METEOROLOGIA", "AEROPLANY");
    //@Autowired
    public StartUp(UserRepository userRepository, PostRepository postRepository, GroupRepository groupRepository, ParticipantRepository participantRepository, ReactionRepository reactionRepository, PrivateMessageRepository privateMessageRepository, ConversationRepository conversationRepository, SignUpFormRepository signUpFormRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.groupRepository = groupRepository;
        this.participantRepository = participantRepository;
        this.reactionRepository = reactionRepository;
        this.privateMessageRepository = privateMessageRepository;
        this.conversationRepository = conversationRepository;
        this.signUpFormRepository = signUpFormRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("---IM GOING---");
        this.loadUserData();
        System.out.println("---IM GOING--- 1 done");
        this.loadReactionData();
        System.out.println("---IM GOING--- 2 done");
        this.loadGroupsData();
        System.out.println("---IM GOING--- 3 done");
        this.loadParticipantsToGroups();
        System.out.println("---IM GOING--- 4 done");
        this.loadPostsToGroups();
        System.out.println("---IM GOING--- 5 done");
        this.groups = groupRepository.findAll();
        this.participants = participantRepository.findAll();
        this.users = userRepository.findAll();
        this.loadDefaultConversation();
        System.out.println("---IM GOING--- 6 done");
    }

    /**
     * create default conversation
     */
    private void loadDefaultConversation() {
        Participant p1 = this.participants.get(0);
        Participant p2 = this.participants.get(6);
        Conversation conversation = new Conversation();
        PrivateMessage message = new PrivateMessage();
        message.setConversation(conversation);
        message.setSender(p1);
        message.setType("CHAT");
        message.setContent("testowa pierwswza wiadoamosc");
        conversation.setParticipants(Arrays.asList(p1, p2));
        conversation.setAvatar("bez awatara");
        conversation.setName("conversation");
        this.participantRepository.saveAll(Arrays.asList(p1, p2));
        this.conversationRepository.save(conversation);
        this.privateMessageRepository.save(message);
    }

    /**
     * POST THINGS
     */
    private void loadPostsToGroups() {
        this.groups = groupRepository.findAll();
        for (Group group : groups) {
            int ile = rand.nextInt(50) + 15;
            for (int i = 0; i < ile; i++) {
                Post post = new Post();
                post.setGroup(group);
                post.setAuthor(participants.get(rand.nextInt(participants.size()-1)));
                post.setContent(this.generateRandomString(rand.nextInt(180) + 20));
                post.setReactionCount(rand.nextInt(30) + 5);
                post.setHashTags(Arrays.asList(new HashTag("#jaja"), new HashTag("#costam")));
                postRepository.save(post);
            }
        }
    }

    private String generateRandomString(int length){
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        return rand.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    /**
     * PARTICIPANTS THINGS
     */
    private void loadParticipantsToGroups() {
        List<String> nicknames = Arrays.asList("andrzej1", "mnarekkk", "lewislaw", "barekkk", "jurekkk", "anmcymon");
        for (int i = 0; i < nicknames.size(); i++) {
            for (Group group : this.groups) {
                Participant part = this.generateBasicParticipantToRandomGroup(nicknames.get(i), group, i);
                this.participants.add(part);
            }
        }
        this.participants = participantRepository.saveAll(this.participants);
    }

    private Participant generateBasicParticipantToRandomGroup(String nickaname, Group group, int index) {
        Participant participant = new Participant();
        participant.setNickname(nickaname);
        participant.setEnabled(true);
        participant.setUser(this.users.get(index));
        participant.setGroup(group);
        participant.getGroup().setParticipantCount(
                participant.getGroup().getParticipantCount()+1
        );
        groupRepository.save(participant.getGroup());
        return participant;
    }

    /**
     * GROUPS THINGS
     */
    private void loadGroupsData() {
        Group gupa = this.generateGroup("dupa");
        Group cos = this.generateGroup("cos");
        Group group1 = this.generateGroup("group1");
        Group group2 = this.generateGroup("group2 cos tam cos tam");
        Group group3 = this.generateGroup("Porzadna grupa");
        this.groups = groupRepository.saveAll(Arrays.asList(gupa, cos, group1, group2, group3));
    }

    private Group generateGroup(String nazwa) {
        Group group = new Group();
        group.setName(nazwa);
        group.setAccept(false);
        group.setCategory(categories.get(rand.nextInt(categories.size()-1)));
        group.setDescription("Grupa o "+ group.getCategory()+ " i okolicznych tematech.");
        group.setPrivate(false);
        group.setForm(false);
        group.setCreatorId(1);
        group.setColor("#FFAACC");
        group.setReaction(this.likes);
        group.setParticipantCount(1);
        group.setInviteCode(UUID.randomUUID().toString());
        group.setImageId("zdjecia");
        return group;
    }


    /**
     * REACTIONS THINGs
     */
    private void loadReactionData() {
        Reaction reaction = new Reaction();
        reaction.setReactionType("Likes");
        this.likes = reactionRepository.save(reaction);
    }

    /**
     *   USER THINGS
     */
    private void loadUserData(){
        User fpmoles = this.generateUserFromEmail("fpmoles@fpmoles.pl", true);
        User jdoe = this.generateBasicUser("jdoe@jdoe.pl");
        User jacek = this.generateBasicUser("jacek@jacek.pl");
        User adam = this.generateBasicUser("adam@adam.pl");
        User kasia = this.generateBasicUser("kasia@kasia.pl");
        User admin = this.generateUserFromEmail("admin@admin.pl", true);
        List<User> user = Arrays.asList(fpmoles, jdoe, jacek, adam, kasia, admin);
        user.forEach(u -> u.setActivated(true));
        this.users = userRepository.saveAll(user);
    }
    private User generateBasicUser(String email){
        return this.generateUserFromEmail(email, false);
    }
    private User generateUserFromEmail(String email, boolean admin){
        RegisterRequest fpmoles = new RegisterRequest();
        fpmoles.setEmail(email);
        fpmoles.setPassword("password");
        fpmoles.setRepassword("password");
        User fpUser = fpmoles.toUser(this.passwordEncoder);
        if(admin){
            fpUser.setAuthorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"),
                    new SimpleGrantedAuthority("ROLE_ADMIN")));
        }
        return fpUser;
    }
}
