iptables -I INPUT -s krlobby.igamecj.com -j REJECT &>/dev/null
iptables -I OUTPUT -s krlobby.igamecj.com -j REJECT &>/dev/null
iptables -I INPUT -s lobby.igamecj.com -j REJECT &>/dev/null
iptables -I OUTPUT -s lobby.igamecj.com -j REJECT &>/dev/null