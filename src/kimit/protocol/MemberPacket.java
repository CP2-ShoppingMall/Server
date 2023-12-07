package kimit.protocol;

import kimit.server.Member;

public class MemberPacket extends Packet
{
	private final Member Member;

	public MemberPacket(Member member)
	{
		super(HeaderCode.MEMBER);
		Member = member;
	}

	public kimit.server.Member getMember()
	{
		return Member;
	}
}
